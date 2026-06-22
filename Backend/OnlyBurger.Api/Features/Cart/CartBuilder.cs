using Microsoft.EntityFrameworkCore;
using OnlyBurger.Api.Data;

namespace OnlyBurger.Api.Features.Cart;

/// <summary>
/// Shared helper that materializes a user's cart into a <see cref="CartDto"/>. Used by the
/// cart query and by the cart commands so they can all return the up-to-date cart.
/// </summary>
internal static class CartBuilder
{
    public static async Task<CartDto> BuildAsync(AppDbContext db, int userId, CancellationToken cancellationToken)
    {
        var items = await db.CartItems
            .AsNoTracking()
            .Where(c => c.UserId == userId)
            .OrderBy(c => c.Id)
            .Select(c => new CartItemDto(
                c.Id,
                c.ProductId,
                c.Product!.Name,
                c.Product.Price,
                c.Quantity,
                c.Product.Price * c.Quantity))
            .ToListAsync(cancellationToken);

        var total = items.Sum(i => i.LineTotal);
        return new CartDto(items, total);
    }
}
