import React, { useCallback, useState } from 'react';
import { observer } from 'mobx-react';
import { useLocation } from 'wouter';
import { useTranslation } from 'react-i18next'
import { Select } from 'antd';
import './AppBar.less';
const { Option } = Select;

const AppBar: React.FC = observer(() => {
  const [location, setLocation] = useLocation();
  // init select language
  const [languageType, setLanguageType] = useState(localStorage.getItem('languageType') || 'zh-CN')
  const { t } = useTranslation()
  const setRoute = useCallback(
    (route: string) => () => {
      setLocation(route);
    },
    [setLocation]
  );
  /**
   * switch language and update localStorage
   */
  const i18Change = (e: string) => {
    localStorage.setItem('languageType', e)
    setLanguageType(e)
    // Refresh directly or through react.createcontext implements no refresh switching
    window.location.reload()
  }
  return (
    <nav className="navigator">
      <div className="navigator-logo" onClick={setRoute('/')}></div>
      <div className="navigator-items">
        <div
          className={ (location == "/" || location == "/graph-management") ? "navigator-item active" : "navigator-item" }
          onClick={setRoute('/graph-management')}
        >
          <span>{t('addition.appbar.graph-manager')}</span>
        </div>

        <div
          className={ location == "/readme/load" ? "navigator-item active" : "navigator-item" }
          onClick={setRoute('/readme/load')}
        >
          <span>{t('addition.appbar.load')}</span>
        </div>
        <div >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
        <div
            className={ location == "/readme/gremlin" ? "navigator-item active" : "navigator-item" }
            onClick={setRoute('/readme/gremlin')}
        >
            <span>{t('addition.appbar.gremlin')}</span>
        </div>

         <div >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
         <div
             className={ location == "/readme/algorithm" ? "navigator-item active" : "navigator-item" }
             onClick={setRoute('/readme/algorithm')}
         >
             <span>{t('addition.appbar.algorithm')}</span>
         </div>

        <div >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
        <div
            className={ location == "/readme/about" ? "navigator-item active" : "navigator-item" }
            onClick={setRoute('/readme/about')}
        >
            <span>{t('addition.appbar.about')}</span>
        </div>
      </div>
      <div className="navigator-additions">
        <span></span>
      </div>
      {/* i18n */}
      <div className="i18n-box">
        <Select
          defaultValue={languageType}
          style={{ width: 120 }}
          size="small"
          onChange={i18Change}
        >
          <Option value="zh-CN">中文</Option>
          <Option value="en-US">English</Option>
        </Select>
      </div>
    </nav>
  );
});

export default AppBar;
