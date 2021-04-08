package com.huafagroup.activiti.domain.dto;

import com.huafagroup.activiti.entity.ModuleTable;

import java.util.List;

/**
 * @author ：linshenghui
 * @date ：2021/4/8 14:13
 * @return :$
 */
public class BizTerminalIndexDto {
    /*党模块*/
    private List<ModuleTable> partyModule;
    /*生产模块*/
    private List<ModuleTable> productionModule;
    /*生活模块*/
    private List<ModuleTable> lifeModule;

    public List<ModuleTable> getPartyModule() {
        return partyModule;
    }

    public void setPartyModule(List<ModuleTable> partyModule) {
        this.partyModule = partyModule;
    }

    public List<ModuleTable> getProductionModule() {
        return productionModule;
    }

    public void setProductionModule(List<ModuleTable> productionModule) {
        this.productionModule = productionModule;
    }

    public List<ModuleTable> getLifeModule() {
        return lifeModule;
    }

    public void setLifeModule(List<ModuleTable> lifeModule) {
        this.lifeModule = lifeModule;
    }
}
