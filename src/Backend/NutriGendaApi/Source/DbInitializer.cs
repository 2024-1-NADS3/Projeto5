using NutriGendaApi.Source.Data;
using NutriGendaApi.Source.Models;

namespace NutriGendaApi.Source
{
    public static class DbInitializer
    {
        public static void Initialize(AppDbContext context)
        {
            context.Database.EnsureCreated();

            // Verifica se já existem nutricionistas cadastrados
            if (!context.Nutritionists.Any())
            {
                var nutritionist = new Nutritionist
                {
                    Name = "Dr. John Doe",
                    Email = "john.doe@example.com",
                    PasswordHash = BCrypt.Net.BCrypt.HashPassword("JohnDoe1234!@#$"),
                    Crn = "CRN123456"
                };

                context.Nutritionists.Add(nutritionist);
                context.SaveChanges();

                // Cria um usuário associado ao nutricionista
                var user = new User
                {
                    Email = "user@example.com",
                    PasswordHash = BCrypt.Net.BCrypt.HashPassword("User1234!@#$"),
                    NutritionistId = nutritionist.Id
                };

                context.Users.Add(user);
                context.SaveChanges();

                // Cria uma dieta associada ao usuário
                var diet = new Diet
                {
                    UserId = user.Id
                };

                context.Diets.Add(diet);
                context.SaveChanges();

                // Cria refeições associadas à dieta
                var mealNames = new string[] { "Café da Manhã", "Lanche", "Almoço" };
                foreach (var mealName in mealNames)
                {
                    var meal = new Meal
                    {
                        Name = mealName,
                        DietId = diet.Id
                    };

                    context.Meals.Add(meal);
                }
                context.SaveChanges();

                // Adiciona alguns alimentos às refeições
                var meals = context.Meals.ToList();
                foreach (var meal in meals)
                {
                    var foodItems = new FoodItem[]
                    {
                        new FoodItem { Name = "Pão", Description = "2 fatias", MealId = meal.Id },
                        new FoodItem { Name = "Leite", Description = "1 copo", MealId = meal.Id }
                    };

                    foreach (var item in foodItems)
                    {
                        context.FoodItems.Add(item);
                    }
                }
                context.SaveChanges();
            }
        }
    }
}
