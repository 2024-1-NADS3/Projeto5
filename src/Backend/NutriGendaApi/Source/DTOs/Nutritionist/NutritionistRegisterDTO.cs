using System.ComponentModel.DataAnnotations.Schema;

namespace NutriGendaApi.Source.DTOs.Nutritionist
{
    public class NutritionistRegisterDTO
    {
        public Guid Id { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        [NotMapped]
        public string? Password { get; set; }
        public string Crn { get; set; }
    }
}
