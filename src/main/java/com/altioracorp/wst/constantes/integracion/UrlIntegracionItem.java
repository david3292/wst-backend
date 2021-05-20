package com.altioracorp.wst.constantes.integracion;

public enum UrlIntegracionItem {
	
	OBTENER_POR_CRITERIO("/GpItems/ByCriteria/"),
	OBTENER_STOCK_POR_ITEMNMBR("/GpItems/QuantitiesByItemnmbr/"),
	OBTENER_STOCK_BIN_POR_ITEMNMBR_AND_LOCNCODE("/GpItems/BinQuantitiesByItemnmbrAndLocncode/"),
	OBTENER_BINS_POR_LOCNCODE("/GpItems/GetBinsByLocncode/${locncode}"),
	INTEGRAR_ITEM_LISTA_DE_PRECIO("/GpItems/IntegrateItemPriceList"),
	OBTENER_BIN_ULTIMO_MOVIMIENTO("/GpItems/GetBinLastMove/${codigoArticulo}/${codigoBodega}"),
	OBTENER_CLASES_ARTICULO("/GpItems/GetItemClasses"),
	OBTENER_UNIDADES_MEDIDA("/GpItems/GetUnitOfMeasure"),
	OBTENER_CATEGORIAS_POR_TIPO("/GpItems/GetItemCategoriesByType/${type}"),
	OBTENER_LISTA_PRECIOS("/GpItems/GetItemPriceList"),
	OBTENER_REPOSICION("/GpItems/GetReplenishmentItems"),
	OBTENER_REPOSICION_POR_ARTICULO_Y_BODEGA("/GpItems/ReplenishmentItemByItemnmbrAndLocncode"),
	INTEGRAR_ARTICULO("/GpItems/CreateNewItem")
	;
	
	private String url;

	private UrlIntegracionItem(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
