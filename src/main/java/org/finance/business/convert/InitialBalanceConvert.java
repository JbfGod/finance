package org.finance.business.convert;

import org.finance.business.entity.InitialBalance;
import org.finance.business.entity.InitialBalanceItem;
import org.finance.business.web.request.AddInitialBalanceRequest;
import org.finance.business.web.request.UpdateInitialBalanceRequest;
import org.finance.business.web.vo.InitialBalanceItemVO;
import org.finance.business.web.vo.InitialBalanceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */
@Mapper
public interface InitialBalanceConvert {

    InitialBalanceConvert INSTANCE = Mappers.getMapper( InitialBalanceConvert.class );

    InitialBalanceItem toInitialBalanceItem(AddInitialBalanceRequest request);

    InitialBalanceItem toInitialBalanceItem(UpdateInitialBalanceRequest request);

    InitialBalanceItemVO toInitialBalanceItemVO(InitialBalanceItem initialBalanceItem);

    InitialBalanceVO toInitialBalanceVO(InitialBalance initialBalance);
}
