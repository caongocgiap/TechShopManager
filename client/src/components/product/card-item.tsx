import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Link } from "react-router-dom"

type Product = {
  id: string
  name: string
  image: string
  price: number
  specs: string[]
}

export const CardItem = ({ product }: { product: Product }) => {
  return (
    <Card className="w-full max-w-sm hover:shadow-lg transition-shadow">
      <Link to={`/products/${product.id}`}>
        <img
          src={product.image}
          alt={product.name}
          className="w-full h-48 object-cover rounded-t-md"
        />
      </Link>
      <CardContent className="p-4">
        <Link to={`/products/${product.id}`}>
          <h3 className="text-lg font-semibold text-foreground hover:text-primary">
            {product.name}
          </h3>
        </Link>

        <ul className="mt-2 text-sm text-muted-foreground space-y-1">
          {product.specs.slice(0, 3).map((spec, index) => (
            <li key={index}>• {spec}</li>
          ))}
        </ul>

        <div className="mt-3 flex items-center justify-between">
          <span className="text-primary font-bold text-lg">
            {product.price.toLocaleString()}₫
          </span>
          <Button asChild>
            <Link to={`/products/${product.id}`}>Xem chi tiết</Link>
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
