# Multi-Provider AI Chat Application

This Spring Boot application integrates with multiple AI APIs (DeepSeek, Claude, Gemini, ChatGPT) to provide a chat interface for users to interact with various large language models.

## Features

- Simple web interface for chatting with AI models
- Support for multiple AI providers (DeepSeek, Claude, Gemini, ChatGPT)
- UI option to choose between different AI providers
- RESTful API endpoints for integration with other applications
- Support for both simple and advanced chat interactions
- Configurable API settings

## Getting Started

### Prerequisites

- Java 23 or higher
- Maven 3.6 or higher
- API keys for the AI providers you want to use

### Configuration

1. Open `src/main/resources/application.properties`
2. Replace the API keys with your actual API keys:
   ```
   deepseek.api.key=your_deepseek_api_key
   claude.api.key=your_claude_api_key
   gemini.api.key=your_gemini_api_key
   chatgpt.api.key=your_chatgpt_api_key
   ```
3. (Optional) Set your default AI service:
   ```
   default.ai.service=DeepSeek
   ```

### Running the Application

```bash
./mvnw spring-boot:run
```

The application will start on port 8080 by default. You can access the web interface at http://localhost:8080/

## API Endpoints

### Available AI Services

```
GET /api/chat/services
```

Returns a list of available AI services.

### Simple Chat

```
POST /api/chat/simple?service=ServiceName
```

Request body:
```json
{
  "message": "Your message here"
}
```

The `service` query parameter is optional. If not provided, the default service will be used.

### Advanced Chat

```
POST /api/chat?service=ServiceName
```

Request body:
```json
[
  {
    "role": "system",
    "content": "You are a helpful assistant."
  },
  {
    "role": "user",
    "content": "Tell me about AI."
  }
]
```

The `service` query parameter is optional. If not provided, the default service will be used.

### Health Check

```
GET /api/chat/health
```

## Project Structure

- `model/` - Data models for requests and responses
- `service/` - Business logic and API integration
- `controller/` - REST API endpoints
- `config/` - Application configuration

## Testing

Run the tests with:

```bash
./mvnw test
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.