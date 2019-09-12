export interface IUrlMapping {
  id?: number;
  urlLong?: string;
  urlShort?: string;
  type?: number;
}

export const defaultValue: Readonly<IUrlMapping> = {};
