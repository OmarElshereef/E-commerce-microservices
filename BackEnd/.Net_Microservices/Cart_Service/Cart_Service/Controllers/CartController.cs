using Cart_Service.Models;
using Cart_Service.Services;
using Microsoft.AspNetCore.Mvc;

namespace Cart_Service.Controllers
{
    [ApiController]
    [Route("api/v1/[controller]")]
    public class CartController : ControllerBase
    {
        private readonly CartService _cartService;

        public CartController(CartService cartService)
        {
            _cartService = cartService;
        }

        // Add or update cart (default 24h TTL)
        [HttpPost("{userId}")]
        public async Task<IActionResult> AddOrUpdateCart(string userId, [FromBody] List<CartItem> cartItems)
        {
            await _cartService.AddOrUpdateCartAsync(userId, cartItems);
            return Ok(new { message = "Cart saved/updated with default 24h TTL" });
        }

        // Simulate order placed (extend TTL → 1 week)
        [HttpPut("{userId}/order")]
        public async Task<IActionResult> OnOrderPlaced(string userId)
        {
            await _cartService.OnOrderPlacedAsync(userId);
            return Ok(new { message = "Cart TTL extended to 1 week" });
        }

        // Simulate payment confirmed (TTL removed → indefinite)
        [HttpPut("{userId}/payment")]
        public async Task<IActionResult> OnPaymentConfirmed(string userId)
        {
            await _cartService.OnPaymentConfirmedAsync(userId);
            return Ok(new { message = "Cart TTL removed (persist indefinitely)" });
        }
    }
}
