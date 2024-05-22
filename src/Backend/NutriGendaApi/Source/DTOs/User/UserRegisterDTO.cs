namespace NutriGendaApi.Source.DTOs.User
{
    public class UserRegisterDTO
    {
        public Guid Id { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public Guid NutritionistId { get; set; }
    }
}
