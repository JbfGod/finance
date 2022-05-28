package org.finance.business.convert;

import org.finance.business.entity.Currency;
import org.finance.business.web.request.AddCurrencyRequest;
import org.finance.business.web.request.UpdateCurrencyRequest;
import org.finance.business.web.vo.CurrencyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */

@Mapper
public interface CurrencyConvert {

    CurrencyConvert INSTANCE = Mappers.getMapper( CurrencyConvert.class );

    Currency toCurrency(AddCurrencyRequest request);

    Currency toCurrency(UpdateCurrencyRequest request);

    CurrencyVO toCurrencyVO(Currency currency);

}
