using AutoMapper;
using NutriGendaApi.Source.DTOs;
using NutriGendaApi.Source.DTOs.Diet;
using NutriGendaApi.Source.DTOs.Nutritionist;
using NutriGendaApi.Source.DTOs.User;
using NutriGendaApi.Source.Models;

namespace NutriGendaApi.Source.Mapper
{
    public class MappingProfile : Profile
    {
        public MappingProfile()
        {
            // Mapeamentos para Diet
            CreateMap<Diet, DietDTO>()
                .ReverseMap();
            CreateMap<Meal, MealDTO>() 
                .ReverseMap();
            CreateMap<FoodItem, FoodItemDTO>()
                .ReverseMap();

            // Mapeamentos para Nutritionist
            CreateMap<Nutritionist, NutritionistRegisterDTO>()
                .ReverseMap();

            CreateMap<Nutritionist, NutritionistLoginDTO>()
                .ReverseMap();
            
            // Mapeamentos para User
            CreateMap<User, UserRegisterDTO>()
                .ReverseMap();

            CreateMap<User, UserLoginDTO>()
                .ReverseMap();
        }
    }
}
