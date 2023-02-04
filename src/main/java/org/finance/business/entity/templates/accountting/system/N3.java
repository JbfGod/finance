package org.finance.business.entity.templates.accountting.system;

import org.finance.business.entity.Report;
import org.finance.business.entity.ReportFormula;
import org.finance.business.entity.Subject;
import org.finance.business.entity.enums.Symbol;
import org.finance.infrastructure.constants.LendingDirection;

import java.util.Arrays;
import java.util.List;

/**
 * @author jiangbangfa
 */
public class N3 extends AbstractSystem {

    public N3() {
        this.subjects = Arrays.asList(
                new Subject("101", "现金", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("102", "银行存款", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("111", "短期投资", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("112", "应收款", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("113", "内部往来", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("121", "库存物资", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("131", "牲畜（禽）资产", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("13101", "幼畜及育肥畜", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("13102", "产役畜", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("132", "林木资产", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("13201", "经济林木", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("13202", "非经济林木", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("141", "长期投资", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("151", "固定资产", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("152", "累计折旧", Subject.Category.ASSETS, LendingDirection.LOAN),
                new Subject("153", "固定资产清理", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("154", "在建工程", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("161", "无形资产", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("171", "拨付所属单位资金", Subject.Category.ASSETS, LendingDirection.BORROW),
                new Subject("201", "短期借款", Subject.Category.FU_ZAI, LendingDirection.LOAN),
                new Subject("202", "应付款", Subject.Category.FU_ZAI, LendingDirection.LOAN),
                new Subject("211", "应付工资", Subject.Category.FU_ZAI, LendingDirection.LOAN),
                new Subject("212", "应付福利费", Subject.Category.FU_ZAI, LendingDirection.LOAN),
                new Subject("221", "长期借款及应付款", Subject.Category.FU_ZAI, LendingDirection.LOAN),
                new Subject("231", "一事一议资金", Subject.Category.FU_ZAI, LendingDirection.LOAN),
                new Subject("241", "专项应付款", Subject.Category.FU_ZAI, LendingDirection.LOAN),
                new Subject("301", "资本", Subject.Category.EQUITY, LendingDirection.LOAN),
                new Subject("311", "公积公益金", Subject.Category.EQUITY, LendingDirection.LOAN),
                new Subject("321", "本年收益", Subject.Category.EQUITY, LendingDirection.LOAN),
                new Subject("322", "收益分配", Subject.Category.EQUITY, LendingDirection.LOAN),
                new Subject("32201", "其他转入", Subject.Category.EQUITY, LendingDirection.LOAN),
                new Subject("32202", "各项分配", Subject.Category.EQUITY, LendingDirection.BORROW),
                new Subject("3220201", "提取公积公益金", Subject.Category.EQUITY, LendingDirection.BORROW),
                new Subject("3220202", "提取应付福利费", Subject.Category.EQUITY, LendingDirection.BORROW),
                new Subject("3220203", "外来投资分利", Subject.Category.EQUITY, LendingDirection.BORROW),
                new Subject("3220204", "农户分配", Subject.Category.EQUITY, LendingDirection.BORROW),
                new Subject("3220205", "其他", Subject.Category.EQUITY, LendingDirection.BORROW),
                new Subject("32203", "未分配收益", Subject.Category.EQUITY, LendingDirection.LOAN),
                new Subject("401", "生产（劳务）成本", Subject.Category.COST, LendingDirection.BORROW),
                new Subject("501", "经营收入", Subject.Category.PROFIT, LendingDirection.LOAN),
                new Subject("502", "经营支出", Subject.Category.PROFIT, LendingDirection.BORROW),
                new Subject("511", "发包及上交收入", Subject.Category.PROFIT, LendingDirection.LOAN),
                new Subject("51101", "承包金", Subject.Category.PROFIT, LendingDirection.LOAN),
                new Subject("51102", "企业上交利润", Subject.Category.PROFIT, LendingDirection.LOAN),
                new Subject("521", "农业税附加返还收入", Subject.Category.PROFIT, LendingDirection.LOAN),
                new Subject("522", "补助收入", Subject.Category.PROFIT, LendingDirection.LOAN),
                new Subject("531", "其他收入", Subject.Category.PROFIT, LendingDirection.LOAN),
                new Subject("541", "管理费用", Subject.Category.PROFIT, LendingDirection.BORROW),
                new Subject("54101", "工资", Subject.Category.PROFIT, LendingDirection.BORROW),
                new Subject("54102", "办公费", Subject.Category.PROFIT, LendingDirection.BORROW),
                new Subject("54103", "差旅费", Subject.Category.PROFIT, LendingDirection.BORROW),
                new Subject("54104", "折旧费", Subject.Category.PROFIT, LendingDirection.BORROW),
                new Subject("54105", "维修费", Subject.Category.PROFIT, LendingDirection.BORROW),
                new Subject("551", "其他支出", Subject.Category.PROFIT, LendingDirection.BORROW),
                new Subject("561", "投资收益", Subject.Category.PROFIT, LendingDirection.LOAN)
        );
        this.reports = Arrays.asList(
                /**
                 * 资产负债表
                 */
                new Report(1, Report.Category.ASSETS, "流动资产：", 0, 1, 0),
                new Report(2, Report.Category.ASSETS, "货币资金", 1, 1, 1).buildBalanceFormulas("101", "102", "109"),
                new Report(3, Report.Category.ASSETS, "应收款项", 2, 1, 1).setReportFormulas(Arrays.asList(
                        new ReportFormula(Symbol.ADD, "114", ReportFormula.NumberRule.DEBIT_BALANCE),
                        new ReportFormula(Symbol.ADD, "113", ReportFormula.NumberRule.BALANCE)
                )),
                new Report(4, Report.Category.ASSETS, "存货", 3, 1, 0).buildBalanceFormulas("121", "122", "124", "125", "127", "128", "401"),
                new Report(5, Report.Category.ASSETS, "流动资产合计", 4, 1, 2).buildAggregateFormulas(2, 3, 4),
                new Report(6, Report.Category.ASSETS, "长期资产：", 0, 1, 0),
                new Report(7, Report.Category.ASSETS, "对外投资", 5, 1, 1).buildBalanceFormulas("131"),
                new Report(8, Report.Category.ASSETS, "农业资产：", 0, 1, 0),
                new Report(9, Report.Category.ASSETS, "牲畜（禽）资产", 6, 1, 1).buildBalanceFormulas("141"),
                new Report(10, Report.Category.ASSETS, "林木资产", 7, 1, 1).buildBalanceFormulas("142"),
                new Report(11, Report.Category.ASSETS, "农业资产合计", 8, 1, 2).buildAggregateFormulas(9, 10),
                new Report(12, Report.Category.ASSETS, "固定资产：", 0, 1, 0),
                new Report(13, Report.Category.ASSETS, "固定资产原值", 9, 1, 1).buildBalanceFormulas("151"),
                new Report(14, Report.Category.ASSETS, "减：累计折旧", 10, 1, 1).buildBalanceFormulas("152"),
                new Report(15, Report.Category.ASSETS, "固定资产净值", 11, 1, 1).buildMinusBalanceFormulas("151", "152"),
                new Report(16, Report.Category.ASSETS, "固定资产清理", 12, 1, 1).buildBalanceFormulas("154"),
                new Report(17, Report.Category.ASSETS, "在建工程", 13, 1, 1).buildBalanceFormulas("153"),
                new Report(18, Report.Category.ASSETS, "固定资产合计", 14, 1, 2).buildAggregateFormulas(15, 16, 17),
                new Report(19, Report.Category.ASSETS, "其他资产：", 0, 1, 0),
                new Report(20, Report.Category.ASSETS, "无形资产", 15, 1, 1).buildBalanceFormulas("161"),
                new Report(21, Report.Category.ASSETS, "长期资产合计", 16, 1, 2).buildAggregateFormulas(7, 9, 10, 15, 16, 17, 20),
                new Report(22, Report.Category.ASSETS, "资产总计", 17, 1, 3).buildAggregateFormulas(5, 21),

                new Report(51, Report.Category.ASSETS, "流动负债：", 0, 1, 0),
                new Report(52, Report.Category.ASSETS, "短期借款", 18, 1, 1).buildBalanceFormulas("201"),
                new Report(53, Report.Category.ASSETS, "应付款项", 19, 1, 1).setReportFormulas(Arrays.asList(
                        new ReportFormula(Symbol.ADD, "114", ReportFormula.NumberRule.CREDIT_BALANCE),
                        new ReportFormula(Symbol.ADD, "211", ReportFormula.NumberRule.BALANCE)
                )),
                new Report(54, Report.Category.ASSETS, "应付工资", 20, 1, 1).buildBalanceFormulas("212"),
                new Report(55, Report.Category.ASSETS, "应付盈余返还", 21, 1, 1).buildBalanceFormulas("221"),
                new Report(56, Report.Category.ASSETS, "应付剩余盈余", 22, 1, 1).buildBalanceFormulas("222"),
                new Report(57, Report.Category.ASSETS, "流动负债合计", 23, 1, 2).buildAggregateFormulas(52, 53, 54, 55, 56),
                new Report(58, Report.Category.ASSETS, "长期负债：", 0, 1, 0),
                new Report(59, Report.Category.ASSETS, "长期借款", 24, 1, 1).buildBalanceFormulas("231"),
                new Report(60, Report.Category.ASSETS, "专项应付款", 26, 1, 1).buildBalanceFormulas("235"),
                new Report(61, Report.Category.ASSETS, "长期负债合计", 26, 1, 2).buildAggregateFormulas(59, 60),
                new Report(62, Report.Category.ASSETS, "负债合计", 27, 1, 2).buildAggregateFormulas(57, 61),

                new Report(66, Report.Category.ASSETS, "所有者权益：", 0, 1, 0),
                new Report(67, Report.Category.ASSETS, "股金", 28, 1, 1).buildBalanceFormulas("301"),
                new Report(68, Report.Category.ASSETS, "专项基金", 29, 1, 1).buildBalanceFormulas("311"),
                new Report(69, Report.Category.ASSETS, "资本公积", 30, 1, 1).buildBalanceFormulas("321"),
                new Report(70, Report.Category.ASSETS, "盈余公积", 31, 1, 1).buildBalanceFormulas("322"),
                new Report(71, Report.Category.ASSETS, "未分配盈余", 32, 1, 1).buildBalanceFormulas("331", "332"),
                new Report(72, Report.Category.ASSETS, "所有者权益合计", 33, 1, 2).buildAggregateFormulas(67, 68, 69, 70, 71),
                new Report(73, Report.Category.ASSETS, "负债和所有者权益总计", 34, 1, 3).buildAggregateFormulas(62, 72)
        );
        initial();
    }
    @Override
    public List<Subject> getSubjects() {
        return this.subjects;
    }

    @Override
    public List<Report> getReports() {
        return this.reports;
    }

}
