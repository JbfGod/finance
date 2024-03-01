import React from 'react'
import {PageContainer} from "@ant-design/pro-layout"

export default function GlobalPageContainer({
                                              header = {title: null, breadcrumb: {}},
                                              children, ...props
                                            }) {
  /*const {route} = useRouteData()
  const location = useLocation()

  const {pathname} = location
  const [activeKey, setActiveKey] = useState('')
  const alive = useAliveController()
  const {dropScope,getCachingNodes} = alive
  const {exitMenus, updateMenus} = useModel('useMenuTabs', (model) => ({
    exitMenus: model.exitMenus,
    updateMenus: model.updateMenus
  }))
  const noCacheRoutes = ['/', '/login', '/Finance', 'Manage']
  useEffect(() => {
    console.log("fuck")
    if (noCacheRoutes.includes(pathname)) {
      return
    }
    const arr = exitMenus.filter((item) => item.key !== pathname)
    if (arr.length === exitMenus.length) {
      const activeMenu = {
        label: route.name,
        key: pathname,
        closable: exitMenus.length > 0
      }
      arr.push(activeMenu)

      updateMenus(arr)
    } else if (exitMenus.length === 1) {
      // 删除时,只剩一个标签去掉删除图标
      const data = exitMenus
      data[0].closable = false
      updateMenus(data)
    }
    setActiveKey(pathname)
  }, [location])

  const onTabChange = (key) => {
    history.push(key)
    setActiveKey(key)
  }*/
  return (
    //<KeepAlive when={true} cacheKey={pathname} id={pathname} name={pathname} saveScrollPosition="screen">
      <PageContainer header={header} {...props} style={{position: "absolute", left: 5, right: 8, bottom: 0, top: 5}}>
        {/*{exitMenus.length > 0 && (
          <Tabs className={styles.pageTabs}
                items={exitMenus}
                onChange={onTabChange}
                type='editable-card'
                hideAdd={true}
                activeKey={activeKey}
                onEdit={(path) => {
                  let activePath = pathname
                  const arr = exitMenus.filter((item, i) => {
                    if (item.key === path) {
                      // 获取前一个标签
                      activePath = exitMenus[i - 1].key
                    }
                    return item.key !== path
                  })
                  // 如果关闭当前标签,展示前一个标签
                  if (path === pathname) {
                    history.push(activePath)
                  }
                  // 关闭页签去掉缓存
                  dropScope(path).then(() => {
                    updateMenus(arr)
                  })
                }}
          />
        )}*/}
        {children}
      </PageContainer>
    //</KeepAlive>
  )
}
