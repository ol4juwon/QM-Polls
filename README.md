QMTech Scheduled Live Polling - Microservices Scaffold
Includes Redis-based idempotency at vote-ingest-service and DB unique constraint at vote-processor.

Services:
- poll-service: manages polls (Postgres)
- vote-publisher: validates and publishes VoteEvent to RabbitMQ. Uses Redis idempotency key with 15s TTL.
- vote-processor: consumes VoteEvent, persists Vote, updates tally. Enforces DB unique constraint (poll_id,user,idempotency_key).
- websocket-service: subscribes to processed events and broadcasts via WebSocket (skeleton).
- docker-compose.yml for local testing.

To run locally:
1. Install Docker.
2. From repository root run:
   docker compose up --build
3. Use APIs:
    - Create poll: POST http://localhost:7001/api/v1/polls
    - Vote: POST http://localhost:7002/api/v1/polls/{pollId}/vote  (include header Idempotency-Key)
4. Websocket endpoint at ws://localhost:7004/ws

Assumptions:
- Users are authorized already

