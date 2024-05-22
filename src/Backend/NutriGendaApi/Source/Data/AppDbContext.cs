using Microsoft.EntityFrameworkCore;
using NutriGendaApi.Source.Models;

namespace NutriGendaApi.Source.Data
{
    public class AppDbContext : DbContext
    {
        public DbSet<User> Users { get; set; }
        public DbSet<Diet> Diets { get; set; }
        public DbSet<Nutritionist> Nutritionists { get; set; }
        public DbSet<Meal> Meals { get; set; }
        public DbSet<FoodItem> FoodItems { get; set; }

        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // User and Nutritionist Relationship
            modelBuilder.Entity<User>()
                .HasOne(u => u.Nutritionist)
                .WithMany()
                .HasForeignKey(u => u.NutritionistId)
                .HasPrincipalKey(n => n.Id)
                .OnDelete(DeleteBehavior.Cascade);

            // Diet to User Relationship
            modelBuilder.Entity<Diet>()
                .HasOne(d => d.User) 
                .WithMany(u => u.Diets) 
                .HasForeignKey(d => d.UserId) 
                .OnDelete(DeleteBehavior.Cascade);

            // Meal to Diet Relationship
            modelBuilder.Entity<Meal>()
                .HasOne(d => d.Diet)
                .WithMany(u => u.Meals)
                .HasForeignKey(d => d.DietId)
                .OnDelete(DeleteBehavior.Cascade);

            // FoodItem to Meal Relationship
            modelBuilder.Entity<FoodItem>()
                .HasOne(f => f.Meal)
                .WithMany(m => m.FoodItems)
                .HasForeignKey(f => f.MealId)
                .OnDelete(DeleteBehavior.Cascade);

            // Custom configurations for Nutritionist
            modelBuilder.Entity<Nutritionist>()
                .Property(n => n.Email)
                .IsRequired()
                .HasMaxLength(255);
        }
    }
}
