package org.springframework.biz.validation.beanvalidation;

import java.io.Serializable;

import javax.validation.constraints.Min;

@CrossFieldMatch.List({
    @CrossFieldMatch(first = "minQty", second = "maxQty",/*operator = CrossFieldOperator.LE ,*/message = "最小数量必须小于等于最大数量")
})
public class ProductPriceQtyRange implements Serializable{
/**
 * 最小数量
 */
@Min(value = 0,message = "最小数量不合法")
private int minQty;
/**
 * 最大数量
 */
@Min(value = 0,message = "最大数量不合法")
private int maxQty;

public int getMinQty() {
    return minQty;
}

public void setMinQty(int minQty) {
    this.minQty = minQty;
}

public int getMaxQty() {
    return maxQty;
}

public void setMaxQty(int maxQty) {
    this.maxQty = maxQty;
}
}