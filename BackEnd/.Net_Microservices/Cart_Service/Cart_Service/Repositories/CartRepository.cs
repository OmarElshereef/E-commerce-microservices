using StackExchange.Redis;
using System.Text.Json;

namespace Cart_Service.Repositories
{
    public class CartRepository
    {
        private readonly IDatabase _db;

        public CartRepository(IConnectionMultiplexer redis)
        {
            _db = redis.GetDatabase();
        }

        private static string GetKey(string userId) => $"cart:{userId}";

        public async Task SetCartAsync(string userId, object cart, TimeSpan? expiry = null)
        {
            var data = JsonSerializer.Serialize(cart);
            await _db.StringSetAsync(GetKey(userId), data, expiry);
        }

        public async Task<string?> GetCartAsync(string userId)
        {
            return await _db.StringGetAsync(GetKey(userId));
        }

        public async Task RemoveCartAsync(string userId)
        {
            await _db.KeyDeleteAsync(GetKey(userId));
        }

        public async Task ExtendCartTTLAsync(string userId, TimeSpan newExpiry)
        {
            await _db.KeyExpireAsync(GetKey(userId), newExpiry);
        }

        public async Task RemoveTTLAsync(string userId)
        {
            await _db.KeyPersistAsync(GetKey(userId)); 
        }
    }
}
