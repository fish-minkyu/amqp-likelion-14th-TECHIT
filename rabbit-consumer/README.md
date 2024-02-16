JobEntity & JobRepository는 Producer와 DB를 공유하기에 같아야 한다.
JobPayload 또한, Consumer에서 Producer가 보낸 dto를 그대로 받기 때문에 같아야 한다.