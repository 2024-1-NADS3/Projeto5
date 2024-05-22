using AutoMapper;
using Microsoft.EntityFrameworkCore;
using NutriGendaApi.Source.Data;
using NutriGendaApi.Source.DTOs.Diet;
using NutriGendaApi.Source.Models;

namespace NutriGendaApi.Source.Services
{
    public class DietService
    {
        private readonly AppDbContext _context;
        private readonly IMapper _mapper;

        public DietService(AppDbContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        /// <summary>
        /// Adiciona uma nova dieta ao banco de dados.
        /// </summary>
        /// <param name="dietDto">Objeto DietDTO contendo todas as informações necessárias.</param>
        /// <returns>A dieta criada com seu ID após a inserção no banco de dados.</returns>
        public async Task<DietDTO> CreateDiet(DietDTO dietDto)
        {
            var diet = _mapper.Map<Diet>(dietDto);
            _context.Diets.Add(diet);
            await _context.SaveChangesAsync();
            return _mapper.Map<DietDTO>(diet);
        }

        /// <summary>
        /// Busca uma dieta específica pelo seu ID.
        /// </summary>
        /// <param name="id">ID da dieta.</param>
        /// <returns>A dieta correspondente ou null se não encontrada.</returns>
        public async Task<DietDTO?> GetDietById(Guid id)
        {
            var diet = await _context.Diets
                .Include(d => d.Meals)
                    .ThenInclude(m => m.FoodItems)
                .FirstOrDefaultAsync(d => d.Id == id);

            if (diet == null)
                return null;

            return _mapper.Map<DietDTO>(diet);
        }

        /// <summary>
        /// Atualiza uma dieta existente com novos dados.
        /// </summary>
        /// <param name="dietDto">Objeto DietDTO atualizado.</param>
        /// <returns>A dieta atualizada.</returns>
        public async Task<DietDTO> UpdateDiet(DietDTO dietDto)
        {
            var diet = await _context.Diets
                .Include(d => d.Meals)
                .ThenInclude(m => m.FoodItems)
                .FirstOrDefaultAsync(d => d.Id == dietDto.Id);

            if (diet == null)
            {
                return null;
            }

            _mapper.Map(dietDto, diet);

            foreach (var mealDto in dietDto.Meals)
            {
                var existingMeal = diet.Meals.FirstOrDefault(m => m.Id == mealDto.Id);
                if (existingMeal != null)
                {
                    _mapper.Map(mealDto, existingMeal);
                    _context.Entry(existingMeal).State = EntityState.Modified;

                    foreach (var foodItemDto in mealDto.FoodItems)
                    {
                        var existingFoodItem = existingMeal.FoodItems.FirstOrDefault(f => f.Id == foodItemDto.Id);
                        if (existingFoodItem != null)
                        {
                            _mapper.Map(foodItemDto, existingFoodItem);
                            _context.Entry(existingFoodItem).State = EntityState.Modified;
                        }
                        else
                        {
                            var newFoodItem = _mapper.Map<FoodItem>(foodItemDto);
                            existingMeal.FoodItems.Add(newFoodItem);
                            _context.Entry(newFoodItem).State = EntityState.Added;
                        }
                    }

                    for (int i = existingMeal.FoodItems.Count - 1; i >= 0; i--)
                    {
                        var foodItem = existingMeal.FoodItems.ElementAt(i);
                        if (!mealDto.FoodItems.Any(dto => dto.Id == foodItem.Id))
                        {
                            existingMeal.FoodItems.Remove(foodItem);
                            _context.Entry(foodItem).State = EntityState.Deleted;
                        }
                    }
                }
                else
                {
                    var newMeal = _mapper.Map<Meal>(mealDto);
                    diet.Meals.Add(newMeal);
                    _context.Entry(newMeal).State = EntityState.Added;
                }
            }

            for (int i = diet.Meals.Count - 1; i >= 0; i--)
            {
                var meal = diet.Meals.ElementAt(i);
                if (!dietDto.Meals.Any(dto => dto.Id == meal.Id))
                {
                    diet.Meals.Remove(meal);
                    _context.Entry(meal).State = EntityState.Deleted;
                }
            }

            _context.Entry(diet).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return _mapper.Map<DietDTO>(diet);
        }

        /// <summary>
        /// Remove uma dieta do banco de dados.
        /// </summary>
        /// <param name="id">ID da dieta a ser removida.</param>
        /// <returns>Confirmação de que a dieta foi removida.</returns>
        public async Task<bool> DeleteDiet(Guid id)
        {
            var diet = await _context.Diets.FindAsync(id);
            if (diet == null)
            {
                return false;
            }
            _context.Diets.Remove(diet);
            await _context.SaveChangesAsync();
            return true;
        }

        /// <summary>
        /// Retorna uma lista de todas as dietas cadastradas.
        /// </summary>
        /// <returns>Lista de dietas.</returns>
        public async Task<List<DietDTO>> GetAllDiets()
        {
            var diets = await _context.Diets
                .Include(d => d.Meals)
                    .ThenInclude(m => m.FoodItems)
                .ToListAsync();

            return _mapper.Map<List<DietDTO>>(diets);
        }

        /// <summary>
        /// Busca todas as dietas associadas a um usuário específico.
        /// </summary>
        /// <param name="userId">ID do usuário.</param>
        /// <returns>Lista de dietas pertencentes ao usuário.</returns>
        public async Task<List<DietDTO>> GetDietsByUserId(Guid userId)
        {
            var diets = await _context.Diets
                .Include(d => d.Meals)
                    .ThenInclude(m => m.FoodItems)
                .Where(d => d.UserId == userId)
                .ToListAsync();

            return _mapper.Map<List<DietDTO>>(diets);
        }
    }

    public class MealService
    {
        private readonly AppDbContext _context;
        private readonly IMapper _mapper;

        public MealService(AppDbContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        public async Task<MealDTO?> GetMealById(Guid mealId)
        {
            var meal = await _context.Meals
                .Include(m => m.FoodItems)
                .SingleOrDefaultAsync(m => m.Id == mealId);
            return meal == null ? null : _mapper.Map<MealDTO>(meal);
        }

        public async Task<MealDTO> CreateMeal(MealDTO mealDto)
        {
            var meal = _mapper.Map<Meal>(mealDto);
            _context.Meals.Add(meal);
            await _context.SaveChangesAsync();
            return _mapper.Map<MealDTO>(meal);
        }

        public async Task<MealDTO?> UpdateMeal(Guid mealId, MealDTO mealDto)
        {
            var meal = await _context.Meals
            .Include(m => m.FoodItems)
            .SingleOrDefaultAsync(m => m.Id == mealId);

            if (meal == null) return null;

            meal.FoodItems.Clear();
            foreach (var foodItemDto in mealDto.FoodItems)
            {
                var foodItem = _mapper.Map<FoodItem>(foodItemDto);
                meal.FoodItems.Add(foodItem);
            }

            _mapper.Map(mealDto, meal);
            _context.Entry(meal).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return _mapper.Map<MealDTO>(meal);
        }

        public async Task<bool> DeleteMeal(Guid mealId)
        {
            var meal = await _context.Meals.FindAsync(mealId);
            if (meal == null) return false;

            _context.Meals.Remove(meal);
            await _context.SaveChangesAsync();
            return true;
        }
    }
}
