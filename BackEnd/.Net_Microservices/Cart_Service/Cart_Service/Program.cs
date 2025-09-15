using Cart_Service.Repositories;
using Cart_Service.Services;
using StackExchange.Redis;
using Steeltoe.Discovery.Eureka;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddEurekaDiscoveryClient();

var redisConnection = builder.Configuration.GetConnectionString("Redis")
    ?? throw new InvalidOperationException("Redis connection string not found in configuration.");

builder.Services.AddSingleton<IConnectionMultiplexer>(sp =>
    ConnectionMultiplexer.Connect(redisConnection)
);

// Register Repository + Service
builder.Services.AddScoped<CartRepository>();
builder.Services.AddScoped<CartService>();

// Add controllers
builder.Services.AddControllers();

var app = builder.Build();

app.MapControllers();
app.Run();
