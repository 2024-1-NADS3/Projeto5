using Microsoft.AspNetCore.Mvc;
using OpenAI_API;
using OpenAI_API.Chat;

namespace NutriGendaApi.Source.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ChatController : ControllerBase
    {
        private readonly OpenAIAPI _openai;

        public ChatController(IConfiguration configuration)
        {
            var apiKey = configuration["OpenAI:ApiKey"];
            _openai = new OpenAIAPI(apiKey);
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody] UserChatRequest request)
        {
            if (request?.Message == null)
            {
                return BadRequest("Message is required.");
            }

            var messages = new List<ChatMessage>
            {
                new ChatMessage(ChatMessageRole.System, "Você é um assistente de dieta."),
                new ChatMessage(ChatMessageRole.User, request.Message)
            };

            var chatRequest = new ChatRequest
            {
                Messages = messages,
                Model = "gpt-3.5-turbo",
                MaxTokens = 500,
                Temperature = 0.7
            };

            try
            {
                var response = await _openai.Chat.CreateChatCompletionAsync(chatRequest);
                var reply = response.Choices[0].Message.TextContent.Trim();

                return Ok(new { reply });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Erro inesperado: " + ex.Message });
            }
        }
    }

    public class UserChatRequest
    {
        public string Message { get; set; }
    }
}
