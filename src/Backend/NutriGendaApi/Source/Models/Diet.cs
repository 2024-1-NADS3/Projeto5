using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace NutriGendaApi.Source.Models
{
    [Table("Diets")]
    public class Diet
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid Id { get; set; }

        [ForeignKey("User")]
        public Guid UserId { get; set; }
        public User? User { get; set; }
        public virtual ICollection<Meal> Meals { get; set; } = new List<Meal>();
    }

    [Table("Meals")]
    public class Meal
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid Id { get; set; }

        [Required]
        public string Name { get; set; }

        public virtual ICollection<FoodItem> FoodItems { get; set; } = new List<FoodItem>();

        public Guid DietId { get; set; }
        public Diet Diet { get; set; }
    }

    [Table("FoodItems")]
    public class FoodItem
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid Id { get; set; }

        [Required]
        public string Name { get; set; } 

        public string Description { get; set; }

        [ForeignKey("Meal")]
        public Guid MealId { get; set; }
        public Meal Meal { get; set; }
    }

}
