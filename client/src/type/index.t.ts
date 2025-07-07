export type Brand = {
  brandId: string;
  brandCode: string;
  brandName: string;
  brandStatus: string;
  orderNumber: number;
  key: string;
};

export type BrandDataTable = {
  orderNumber: number;
  brandId: string;
  brandCode: string;
  brandName: string;
  brandStatus: string;
}

export type ResponseFetchListBrand = {
  orderNumber?: number;
  brandId: string;
  brandCode: string;
  brandName: string;
  brandStatus?: string;
}

export type ModifyBrand = {
  brandCode: string;
  brandName: string;
}







export type Category = {
  categoryId: string;
  categoryCode: string;
  categoryName: string;
  categoryStatus: string;
  orderNumber: number;
  key: string;
};

export type OperationSystem = {
  operationSystemId: string;
  operationSystemCode: string;
  operationSystemName: string;
  operationSystemStatus: string;
  orderNumber: number;
  key: string;
};

export type ScreenResolution = {
  screenResolutionId: string;
  screenResolutionCode: string;
  screenResolutionName: string;
  screenResolutionWidth: number;
  screenResolutionHeight: number;
  screenResolutionAspectRatio: string;
  screenResolutionStatus: string;
  orderNumber: number;
  key: string;
};

export type Cpu = {
  cpuId: string;
  cpuModel: string;
  cpuManufacturerId: string;
  cpuSeries: string;
  cpuGeneration: string;
  cpuCores: number;
  cpuThreads: number;
  cpuBaseClock: number;
  cpuTurboClock: number;
  cpuCacheSize: number;
  cpuTdpWatt: number;
  cpuIntegratedGpu: string;
  cpuArchitecture: string;
  cpuReleaseYear: number;
  cpuStatus: string;
  orderNumber: number;
  key: string;
};

export type Manufacturer = {
  manufacturerId: string;
  manufacturerCode: string;
  manufacturerName: string;
  manufacturerCountry: string;
  manufacturerWebsite: string;
  manufacturerDescription: string;
  manufacturerStatus: string;
  orderNumber: number;
  key: string;
};