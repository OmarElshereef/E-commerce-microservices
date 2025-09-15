using Cart_Service.Repositories;

namespace Cart_Service.Services
{
    public class CartService
    {
        private readonly CartRepository _repo;

        public CartService(CartRepository repo)
        {
            _repo = repo;
        }

        public async Task AddOrUpdateCartAsync(string userId, object cart)
        {
            // Default: 24 hours
            await _repo.SetCartAsync(userId, cart, TimeSpan.FromHours(24));
        }

        public async Task OnOrderPlacedAsync(string userId)
        {
            // Extend TTL to 1 week
            await _repo.ExtendCartTTLAsync(userId, TimeSpan.FromDays(7));
        }

        public async Task OnPaymentConfirmedAsync(string userId)
        {
            // Remove TTL (indefinite persistence)
            await _repo.RemoveTTLAsync(userId);
        }
    }
}
