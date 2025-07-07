import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
import { useState } from "react";
import BrandFilter from "@/components/filter/brand-filter";
import { FilterIcon } from "lucide-react";
import { ComboboxFilter } from "@/components/filter/combobox-filter";
import { CardItem } from "@/components/product/card-item";
import { PaginationDemo } from "@/components/pagination/pagination";

const carouselImages = [
  {
    src: "https://i.pinimg.com/736x/89/e4/1b/89e41b7c82f66dd4109dc7c86f2f8338.jpg",
    alt: "Laptop trên bàn làm việc",
  },
  {
    src: "https://i.pinimg.com/736x/43/56/24/4356249b2cdcf1874928283a1e23d3e3.jpg",
    alt: "Linh kiện máy tính",
  },
  {
    src: "https://i.pinimg.com/736x/3d/b8/c0/3db8c0462e29aedae6af2ea51db0dffe.jpg",
    alt: "Cửa hàng máy tính",
  },
];

const frameworks = [
  {
    value: "next.js",
    label: "Next.js",
  },
  {
    value: "sveltekit",
    label: "SvelteKit",
  },
  {
    value: "nuxt.js",
    label: "Nuxt.js",
  },
  {
    value: "remix",
    label: "Remix",
  },
  {
    value: "astro",
    label: "Astro",
  },
];

const sampleProduct = {
  id: "laptop-acer-aspire-5",
  name: "Acer Aspire 5 A515 - Ryzen 5, 16GB, SSD 512GB",
  image:
    "https://cdn2.fptshop.com.vn/unsafe/1920x0/filters:format(webp):quality(75)/2021_asus_tuf_gaming_a15_fa506_1_dce6fe4658.png",
  price: 13990000,
  specs: [
    "CPU: AMD Ryzen 5 5500U",
    "RAM: 16GB DDR4",
    "SSD: 512GB NVMe",
    "Màn hình: 15.6'' Full HD",
  ],
};

const products = [sampleProduct, sampleProduct, sampleProduct, sampleProduct, sampleProduct, sampleProduct, sampleProduct, sampleProduct, sampleProduct]

const HomePage = () => {
  const [selectedBrands, setSelectedBrands] = useState<string[]>([]);

  return (
    <>
      <div className="max-w-full mx-auto">
        <Carousel>
          <CarouselContent>
            {carouselImages.map((image, index) => (
              <CarouselItem key={index}>
                <img
                  src={image.src}
                  alt={image.alt}
                  className="w-full h-96 object-cover rounded-lg"
                />
              </CarouselItem>
            ))}
          </CarouselContent>
          <CarouselPrevious />
          <CarouselNext />
        </Carousel>
      </div>

      <div className="flex justify-center mt-4">
        <BrandFilter selected={selectedBrands} onChange={setSelectedBrands} />
      </div>

      <div className="mt-4 space-y-2">
        <div className="flex items-center">
          <FilterIcon className="w-5 h-5 text-foreground" />
          <h2 className="font-bold text-xl ms-2 text-foreground">Bộ lọc</h2>
        </div>
        <div className="flex flex-wrap gap-4">
          <ComboboxFilter
            options={frameworks}
            placeholder="Kích thước màn hình"
          />
          <ComboboxFilter options={frameworks} placeholder="Tần số quét" />
          <ComboboxFilter options={frameworks} placeholder="CPU" />
          <ComboboxFilter options={frameworks} placeholder="GPU" />
          <ComboboxFilter options={frameworks} placeholder="RAM" />
          <ComboboxFilter options={frameworks} placeholder="Ổ cứng" />
          <ComboboxFilter options={frameworks} placeholder="Card đồ họa" />
          <ComboboxFilter options={frameworks} placeholder="Bảo mật" />
        </div>
      </div>

      <h1 className="font-bold text-3xl text-foreground mt-5">Laptop Gamming</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 mt-4">
        {products.map((product) => (
          <CardItem key={product.id} product={product} />
        ))}
      </div>
      <div className="mt-4">
        <PaginationDemo />
      </div>
    </>
  );
};

export default HomePage;
