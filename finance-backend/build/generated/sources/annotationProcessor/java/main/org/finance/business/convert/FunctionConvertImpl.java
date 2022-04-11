package org.finance.business.convert;

import javax.annotation.Generated;
import org.finance.business.entity.Function;
import org.finance.business.web.vo.TreeFunctionVO;
import org.finance.business.web.vo.UserOwnedMenuVO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-11T15:10:11+0800",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.1.jar, environment: Java 1.8.0_261 (Oracle Corporation)"
)
public class FunctionConvertImpl implements FunctionConvert {

    @Override
    public UserOwnedMenuVO toUserOwnedMenuVO(Function f) {
        if ( f == null ) {
            return null;
        }

        UserOwnedMenuVO userOwnedMenuVO = new UserOwnedMenuVO();

        userOwnedMenuVO.setKey( f.getNumber() );
        userOwnedMenuVO.setPath( f.getUrl() );
        userOwnedMenuVO.setParentKey( f.getParentNumber() );
        userOwnedMenuVO.setName( f.getName() );
        userOwnedMenuVO.setIcon( f.getIcon() );

        return userOwnedMenuVO;
    }

    @Override
    public TreeFunctionVO toTreeFunctionVO(Function function) {
        if ( function == null ) {
            return null;
        }

        TreeFunctionVO treeFunctionVO = new TreeFunctionVO();

        treeFunctionVO.setId( function.getId() );
        treeFunctionVO.setNumber( function.getNumber() );
        treeFunctionVO.setName( function.getName() );
        treeFunctionVO.setParentNumber( function.getParentNumber() );
        treeFunctionVO.setHasLeaf( function.getHasLeaf() );
        treeFunctionVO.setLevel( function.getLevel() );
        treeFunctionVO.setType( function.getType() );
        treeFunctionVO.setUrl( function.getUrl() );
        treeFunctionVO.setPermitCode( function.getPermitCode() );
        treeFunctionVO.setSortNum( function.getSortNum() );

        return treeFunctionVO;
    }
}
