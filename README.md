### 주문관리 시스템

- external-api
  <br><br>
   mock 역할을 하는 api로써, oms에서 조회 할 주문 데이터 제공
   <br><br>
- oms-api
  <br><br>
  주문 관리 시스템 api



<br><br>
### 다이어그램
![image](https://github.com/user-attachments/assets/d4c4b4e1-f115-4919-8260-fcfdaf521068)


<br><br>
OrderFacade를 통해 데이터 처리 부 / 데이터 저장부 로직호출

```
@Component
class OrderFacade(
    private val orderDataProcessor: OrderDataProcessor,
    private val orderService: OrderService
) {
    @Value("\${order-api.uri}")
    private lateinit var ORDER_API_URL: String

    fun fetchOrders(date: LocalDate) {
        val orders = orderDataProcessor.processData(date, ORDER_API_URL)

        orders.forEach {
            orderService.save(it)
        }
    }
}
```
<br><br>

### 주문데이터 처리 및 예외처리
- restTempalte이용하여 data 받아와서 order로 convert
- spring retry를 이용하여 io exception 발생 시 재시도 로직 수행 / Parsing 에러 발생 시 재시도 x
- 다른 예외의 경우 ControllerAdvice를 이용
```
@Component
class OrderDataProcessorImpl(
    private val restTemplate: RestTemplate
) : OrderDataProcessor {
    private val logger: Logger = LoggerFactory.getLogger(RestTemplateConfig::class.java)

    @Retryable(
        maxAttempts = 3,
        backoff = Backoff(delay = 1000),
        include = [IOException::class],
        exclude = [HttpMessageConversionException::class]
    )
    override fun processData(date: LocalDate, url: String): List<Order> {
        val (entity, uri) = buildOrderUri(date, url)

        lateinit var responseEntity: ResponseEntity<List<OrderResponse>>

        try {
            responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                object : ParameterizedTypeReference<List<OrderResponse>>() {}
            )
        } catch (e: HttpMessageConversionException) {
            logger.error("json parse failed..")
            //todo noti logic
            return emptyList()
        }

        val orderResponses = responseEntity.body

        return orderResponses?.map {
            Order(
                orderId = it.id,
                username = it.orderer.username,
                createdAt = it.orderBase.createdAt,
                state = OrderState.valueOf(it.orderBase.status)
            )
        } ?: emptyList()
    }

    private fun buildOrderUri(date: LocalDate, url: String): Pair<HttpEntity<String>, URI> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val entity = HttpEntity<String>(headers)
        val params = mapOf("date" to date)

        val uri = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("date", "{date}")
            .buildAndExpand(params)
            .toUri()
        return Pair(entity, uri)
    }
}

```




