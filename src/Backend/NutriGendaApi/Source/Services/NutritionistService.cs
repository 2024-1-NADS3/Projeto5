using AutoMapper;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using NutriGendaApi.Source.Data;
using NutriGendaApi.Source.DTOs.Nutritionist;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace NutriGendaApi.Source.Services
{
    public class NutritionistService
    {
        private readonly AppDbContext _context;
        private readonly IMapper _mapper;

        public NutritionistService(AppDbContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        /// <summary>
        /// Cria um novo nutricionista no banco de dados.
        /// </summary>
        /// <param name="nutritionistDto">O DTO do nutricionista a ser criado.</param>
        /// <returns>O DTO do nutricionista criado com seu ID.</returns>
        public async Task<NutritionistRegisterDTO> CreateNutritionist(NutritionistRegisterDTO nutritionistDto)
        {
            var nutritionist = _mapper.Map<Nutritionist>(nutritionistDto);

            // Hash da senha e armazenamento seguro
            nutritionist.PasswordHash = BCrypt.Net.BCrypt.HashPassword(nutritionistDto.Password);

            _context.Nutritionists.Add(nutritionist);
            await _context.SaveChangesAsync();

            var resultDto = _mapper.Map<NutritionistRegisterDTO>(nutritionist);
            return resultDto;
        }



        /// <summary>
        /// Busca um nutricionista pelo ID.
        /// </summary>
        /// <param name="id">ID do nutricionista.</param>
        /// <returns>O DTO do nutricionista encontrado ou null se não existir.</returns>
        public async Task<NutritionistRegisterDTO> GetNutritionistById(Guid id)
        {
            var nutritionist = await _context.Nutritionists.FindAsync(id);
            return _mapper.Map<NutritionistRegisterDTO>(nutritionist);
        }

        /// <summary>
        /// Atualiza informações de um nutricionista existente.
        /// </summary>
        /// <param name="nutritionistDto">DTO do nutricionista com informações atualizadas.</param>
        /// <returns>O DTO do nutricionista atualizado.</returns>
        public async Task<NutritionistRegisterDTO> UpdateNutritionist(NutritionistRegisterDTO nutritionistDto)
        {
            var nutritionist = await _context.Nutritionists.FindAsync(nutritionistDto.Id);
            if (nutritionist == null)
            {
                return null;
            }
            _mapper.Map(nutritionistDto, nutritionist);
            _context.Entry(nutritionist).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return _mapper.Map<NutritionistRegisterDTO>(nutritionist);
        }

        /// <summary>
        /// Deleta um nutricionista pelo ID.
        /// </summary>
        /// <param name="id">ID do nutricionista a ser deletado.</param>
        /// <returns>True se o nutricionista foi deletado com sucesso, false caso contrário.</returns>
        public async Task<bool> DeleteNutritionist(Guid id)
        {
            var nutritionist = await _context.Nutritionists.FindAsync(id);
            if (nutritionist == null)
            {
                return false;
            }
            _context.Nutritionists.Remove(nutritionist);
            await _context.SaveChangesAsync();
            return true;
        }

        /// <summary>
        /// Lista todos os nutricionistas registrados no sistema.
        /// </summary>
        /// <returns>Lista de DTOs de nutricionistas.</returns>
        public async Task<List<NutritionistRegisterDTO>> GetAllNutritionists()
        {
            var nutritionists = await _context.Nutritionists.ToListAsync();
            return _mapper.Map<List<NutritionistRegisterDTO>>(nutritionists);
        }

        /// <summary>
        /// Autentica um nutricionista usando e-mail e senha.
        /// </summary>
        /// <param name="email">E-mail do nutricionista.</param>
        /// <param name="password">Senha do nutricionista.</param>
        /// <returns>O DTO do nutricionista autenticado ou null se a autenticação falhar.</returns>
        public async Task<NutritionistRegisterDTO?> Authenticate(string email, string password)
        {
            var nutritionist = await _context.Nutritionists
                                             .SingleOrDefaultAsync(n => n.Email == email);

            if (nutritionist != null && BCrypt.Net.BCrypt.Verify(password, nutritionist.PasswordHash))
            {
                return _mapper.Map<NutritionistRegisterDTO>(nutritionist);
            }

            return null;
        }

        public string GenerateJwtToken(NutritionistRegisterDTO nutritionist)
        {
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes("ZGYLlJHrJJOJ8kKwr934PuTngXFyQFUsqbaPDGDbKrI"));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.Email, nutritionist.Email),
                new Claim("Id", nutritionist.Id.ToString())
            };

            var token = new JwtSecurityToken(
                claims: claims,
                expires: DateTime.Now.AddHours(3),
                signingCredentials: credentials
            );

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}
