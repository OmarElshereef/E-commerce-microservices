

namespace Cart_Service.Models
{
    public class CartItem
    {
        public string Id { get; set; } = Guid.NewGuid().ToString(); 

        public int UserId { get; set; }   

        public int ProductId { get; set; } 

        public int Quantity { get; set; }   

        public DateTime ExpiryTime { get; set; } = DateTime.UtcNow.AddHours(24); 
    }
}
