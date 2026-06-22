namespace OnlyBurger.Api.Domain.Entities;

/// <summary>
/// An entry in a user's shopping cart. The cart is the staging area used to build an
/// order before checkout. One row per (user, product) pair.
/// </summary>
public class CartItem
{
    public int Id { get; set; }
    public int UserId { get; set; }
    public int ProductId { get; set; }
    public int Quantity { get; set; }

    // Navigation
    public User? User { get; set; }
    public Product? Product { get; set; }
}
