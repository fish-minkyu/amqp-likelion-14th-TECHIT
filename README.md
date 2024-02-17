# AMQP (Rabbitmq)

- 2024.02.14 ~ 02.15 `14주차`
- 02.14 - RabbitMQ: Competing Consumers
- 02.15 - RabbitMQ: Publish Subscribe

`2월 14일`
<details>
<summary><strong>Producer Page</strong></summary>

- Competing Consumers
<div>JobPayload: MQ에게 요청하는 작업 정보를 나타냄</div>
<div>JobRequest: 사용자가 MQ에게 처리를 요청</div>
<div>JobStatus: 사용자가 결과 처리 상태를 확인할 수 있음</div>
<div>JobEntity</div>
<div>JobRepository</div>
<div>ProducerConfig</div>
<div>ProducerService</div>
<div>ProducerController</div>
</details>

<details>
<summary><strong>Consumer Page</strong></summary>

- RabbitMQ_Competing Consumers
<div>JobPayload: MQ에게 요청하는 작업 정보를 나타냄</div>
<div>ConsumerConfig</div>
<div>ConsumerService</div>
</details>

`2월 15일`

<details>
<summary><strong>Publish Page</strong></summary>

- Fanout Exchange & Direct Exchange & Topic Exchange

<div>PublisherConfig</div>
<div>PublisherService</div>
<div>PublisherController</div>
</details>

<details>
<summary><strong>Subscribe Page</strong></summary>

- Fanout Exchange & Direct Exchange & Topic Exchange

<div>SubscriberConfig</div>
<div>SubscriberService</div>
</details>

# README.md

- [RabbitMQ_Producer_README.md](/rabbit-producer/README.md)

- [RabbitMQ_Consumer_README.md](/rabbit-consumer/README.md)


# GitHub
- 강사님 GitHub
[likelion-backend-8-amqp](https://github.com/edujeeho0/likelion-backend-8-amqp)