import {useCallback, useState} from "react";

export default () => {
  const [exitMenus, setExitMenus] = useState([]);

  // 改变缓存菜单
  const updateMenus = useCallback((menus) => {
    setExitMenus(menus);
  }, [])

  return {
    exitMenus,
    updateMenus,
  }

}
