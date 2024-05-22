using NutriGendaApi.Source.Models;

namespace NutriGendaApi.Source.DTOs.Diet
{
    public class DietDTO
    {
        public Guid Id { get; set; }
        public Guid UserId { get; set; }
        public virtual ICollection<MealDTO> Meals { get; set; } = new List<MealDTO>();
    }

    public class MealDTO
    {
        public Guid Id { get; set; }
        public string Name { get; set; }
        public List<FoodItemDTO> FoodItems { get; set; } = new List<FoodItemDTO>();
        public Guid DietId { get; set; }
    }

    public class FoodItemDTO
    {
        public Guid Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
    }
}
