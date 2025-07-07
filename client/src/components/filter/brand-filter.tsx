import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";

const brands = [
  {
    value: "acer",
    logo: "https://images.acer.com/is/content/acer/acer-4",
  },
  {
    value: "asus",
    logo: "https://dlcdnimgs.asus.com/images/logo/logo-blue001.svg",
  },
  {
    value: "dell",
    logo: "https://cdn2.fptshop.com.vn/unsafe/128x0/filters:format(webp):quality(75)/small/logo_dell_ngang_7c71eaa28a.png",
  },
  {
    value: "hp",
    logo: "https://cdn2.fptshop.com.vn/unsafe/128x0/filters:f…p):quality(75)/small/logo_hp_ngang_a7a7bf1976.png",
  },
  {
    value: "lenovo",
    logo: "https://p4-ofp.static.pub/fes/cms/2022/11/14/h82es5y402b4rh1089sf86ay7n9sdl721044.png",
  },
];

const BrandFilter = ({
  selected,
  onChange,
}: {
  selected: string[];
  onChange: (values: string[]) => void;
}) => {
  return (
    <ToggleGroup
      type="multiple"
      value={selected}
      onValueChange={onChange}
      className="flex flex-wrap justify-center gap-3 py-4"
    >
      {brands.map((brand) => (
        <ToggleGroupItem
          key={brand.value}
          value={brand.value}
          className="border rounded-lg p-2 hover:bg-muted data-[state=on]:bg-primary/10"
        >
          <img
            src={brand.logo}
            alt={brand.value}
            className="w-16 h-6 object-contain"
          />
        </ToggleGroupItem>
      ))}
    </ToggleGroup>
  );
};

export default BrandFilter;