using Microsoft.AspNetCore.Mvc;
using NutriGendaApi.Source.DTOs.Diet;
using NutriGendaApi.Source.Services;

namespace NutriGendaApi.Source.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class DietController : ControllerBase
    {
        private readonly DietService _dietService;

        public DietController(DietService dietService)
        {
            _dietService = dietService;
        }

        /// <summary>
        /// Retorna todas as dietas cadastradas.
        /// </summary>
        /// <returns>Uma lista de DTOs de dietas.</returns>
        [HttpGet]
        public async Task<IActionResult> GetAllDiets()
        {
            var diets = await _dietService.GetAllDiets();
            return Ok(diets);
        }

        /// <summary>
        /// Retorna uma dieta específica pelo ID.
        /// </summary>
        /// <param name="id">O ID da dieta.</param>
        /// <returns>O DTO da dieta correspondente ou NotFound se não encontrada.</returns>
        [HttpGet("{id}")]
        public async Task<IActionResult> GetDietById(Guid id)
        {
            var diet = await _dietService.GetDietById(id);
            if (diet == null)
                return NotFound();
            return Ok(diet);
        }

        /// <summary>
        /// Cria uma nova dieta.
        /// </summary>
        /// <param name="dietDto">O DTO da dieta a ser criada.</param>
        /// <returns>O DTO da dieta criada.</returns>
        [HttpPost]
        public async Task<IActionResult> CreateDiet([FromBody] DietDTO dietDto)
        {
            var createdDiet = await _dietService.CreateDiet(dietDto);
            return CreatedAtAction(nameof(GetDietById), new { id = createdDiet.Id }, createdDiet);
        }

        /// <summary>
        /// Atualiza uma dieta existente.
        /// </summary>
        /// <param name="id">O ID da dieta a ser atualizada.</param>
        /// <param name="dietDto">O DTO com os novos dados para a dieta.</param>
        /// <returns>NoContent se a atualização foi bem-sucedida, NotFound se a dieta não foi encontrada.</returns>
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateDiet(Guid id, [FromBody] DietDTO dietDto)
        {
            if (id != dietDto.Id)
                return BadRequest("ID mismatch");

            var existingDiet = await _dietService.GetDietById(id);
            if (existingDiet == null)
                return NotFound();

            var updatedDiet = await _dietService.UpdateDiet(dietDto);
            if (updatedDiet == null)
                return NotFound();

            return NoContent();
        }

        /// <summary>
        /// Deleta uma dieta.
        /// </summary>
        /// <param name="id">O ID da dieta a ser deletada.</param>
        /// <returns>NoContent se deletado com sucesso, NotFound se não encontrada.</returns>
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDiet(Guid id)
        {
            var success = await _dietService.DeleteDiet(id);
            if (!success)
                return NotFound();

            return NoContent();
        }

        /// <summary>
        /// Retorna as dietas de um usuário específico.
        /// </summary>
        /// <param name="userId">O ID do usuário.</param>
        /// <returns>Uma lista de DTOs de dietas do usuário.</returns>
        [HttpGet("user/{userId}")]
        public async Task<IActionResult> GetDietsByUserId(Guid userId)
        {
            var diets = await _dietService.GetDietsByUserId(userId);
            return Ok(diets);
        }
    }
}
