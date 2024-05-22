using Microsoft.AspNetCore.Mvc;
using NutriGendaApi.Source.DTOs.Nutritionist;
using NutriGendaApi.Source.Services;


namespace NutriGendaApi.Source.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class NutritionistController : ControllerBase
    {
        private readonly NutritionistService _nutritionistService;

        public NutritionistController(NutritionistService nutritionistService)
        {
            _nutritionistService = nutritionistService;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] NutritionistLoginDTO nutritionistLoginDto)
        {
            var user = await _nutritionistService.Authenticate(nutritionistLoginDto.Email, nutritionistLoginDto.Password);
            if (user != null)
            {
                var token = _nutritionistService.GenerateJwtToken(user);
                return Ok(new { token, userId = user.Id });
            }
            else
            {
                return Unauthorized();
            }
        }

        /// <summary>
        /// Retorna todos os nutricionistas cadastrados.
        /// </summary>
        /// <returns>Uma lista de DTOs de nutricionistas.</returns>
        [HttpGet]
        public async Task<IActionResult> GetAllNutritionists()
        {
            var nutritionists = await _nutritionistService.GetAllNutritionists();
            return Ok(nutritionists);
        }

        /// <summary>
        /// Retorna um nutricionista específico pelo ID.
        /// </summary>
        /// <param name="id">O ID do nutricionista.</param>
        /// <returns>O DTO do nutricionista encontrado ou NotFound se não existir.</returns>
        [HttpGet("{id}")]
        public async Task<IActionResult> GetNutritionistById(Guid id)
        {
            var nutritionist = await _nutritionistService.GetNutritionistById(id);
            if (nutritionist == null)
                return NotFound();
            return Ok(nutritionist);
        }

        /// <summary>
        /// Cria um novo nutricionista.
        /// </summary>
        /// <param name="nutritionistDto">DTO do nutricionista a ser criado.</param>
        /// <returns>O DTO do nutricionista criado.</returns>
        [HttpPost]
        public async Task<IActionResult> CreateNutritionist([FromBody] NutritionistRegisterDTO nutritionistDto)
        {
            var createdNutritionist = await _nutritionistService.CreateNutritionist(nutritionistDto);
            return CreatedAtAction(nameof(GetNutritionistById), new { id = createdNutritionist.Id }, createdNutritionist);
        }

        /// <summary>
        /// Atualiza um nutricionista existente.
        /// </summary>
        /// <param name="id">O ID do nutricionista a ser atualizado.</param>
        /// <param name="nutritionistDto">DTO com dados atualizados do nutricionista.</param>
        /// <returns>NoContent se a atualização for bem-sucedida, NotFound se não encontrado.</returns>
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateNutritionist(Guid id, [FromBody] NutritionistRegisterDTO nutritionistDto)
        {
            if (id != nutritionistDto.Id)
                return BadRequest("ID mismatch");

            var existingNutritionist = await _nutritionistService.GetNutritionistById(id);
            if (existingNutritionist == null)
                return NotFound();

            await _nutritionistService.UpdateNutritionist(nutritionistDto);
            return NoContent();
        }

        /// <summary>
        /// Deleta um nutricionista pelo ID.
        /// </summary>
        /// <param name="id">O ID do nutricionista a ser deletado.</param>
        /// <returns>NoContent se deletado com sucesso, NotFound se não encontrado.</returns>
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteNutritionist(Guid id)
        {
            var success = await _nutritionistService.DeleteNutritionist(id);
            if (!success)
                return NotFound();

            return NoContent();
        }
    }
}
