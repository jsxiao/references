package com.aggrepoint.dao;

import java.util.Stack;

public class CountHelper {
	static final int STATE_SELECT_START = 0;
	static final int STATE_SELECT = 1;
	static final int STATE_FROM = 2;
	static final int STATE_ORDER = 3;
	static final int STATE_QUOTE = 10;
	static final int STATE_PARENTESIS = 11;

	static final char[] SELECT = "SELECT ".toCharArray();
	static final char[] FROM = "FROM ".toCharArray();
	static final char[] ORDER_BY = "ORDER BY ".toCharArray();
	static final char[] DOUBLE_QUOTE = "''".toCharArray();

	private String select;
	private String from;
	private String order;

	private int match(char[] chars, int st, char[] match) {
		for (int i = 0; i < match.length; i++) {
			if (chars.length <= st + i)
				return -1;
			if (chars[st + i] != match[i])
				return -1;
			if (match[i] == ' ')
				while (chars.length <= st + i + 1 && chars[st + i + 1] == ' ')
					st++;
		}

		return st + match.length - 1;
	}

	public CountHelper(String sql) {
		Stack<Integer> state = new Stack<Integer>();
		state.push(STATE_SELECT_START);

		sql = sql.trim();
		char[] chars = sql.toUpperCase().toCharArray();

		int selectSt, selectEd, fromSt, fromEd, orderSt;
		selectSt = selectEd = fromSt = fromEd = orderSt = -1;

		for (int i = 0; i < chars.length; i++) {
			int posi;

			switch (state.peek()) {
			case STATE_SELECT_START:
				posi = match(chars, 0, SELECT);
				if (posi > 0)
					i = posi;
				else
					i--;
				state.pop();
				state.push(STATE_SELECT);
				selectSt = i + 1;
				break;
			case STATE_SELECT:
				switch (chars[i]) {
				case '(':
					state.push(STATE_PARENTESIS);
					break;
				case '\'':
					state.push(STATE_QUOTE);
					break;
				case 'F':
					if (i == 0 || i > 0 && chars[i - 1] == ' ') {
						posi = match(chars, i, FROM);
						if (posi > 0) {
							selectEd = i - 1;
							state.pop();
							state.push(STATE_FROM);
							i = posi;
							fromSt = i + 1;
						}
					}
					break;
				}
				break;
			case STATE_FROM:
				switch (chars[i]) {
				case '(':
					state.push(STATE_PARENTESIS);
					break;
				case '\'':
					state.push(STATE_QUOTE);
					break;
				case 'O':
					posi = match(chars, i, ORDER_BY);
					if (posi > 0) {
						fromEd = i - 1;
						state.pop();
						state.push(STATE_ORDER);
						i = posi;
						orderSt = i + 1;
					}
					break;
				}
				break;
			case STATE_QUOTE:
				switch (chars[i]) {
				case '\'':
					posi = match(chars, i, DOUBLE_QUOTE);
					if (posi > 0)
						i = posi;
					else
						state.pop();
					break;
				}
				break;
			case STATE_PARENTESIS:
				switch (chars[i]) {
				case '(':
					state.push(STATE_PARENTESIS);
					break;
				case '\'':
					state.push(STATE_QUOTE);
					break;
				case ')':
					state.pop();
					break;
				}
				break;
			}

			if (state.peek() == STATE_ORDER)
				break;
		}

		if (state.peek() == STATE_FROM)
			fromEd = chars.length;

		if (fromEd == -1)
			return;

		if (selectEd == -1) // hql可能没有select
			select = "";
		else
			select = sql.substring(selectSt, selectEd);
		from = sql.substring(fromSt, fromEd);

		if (orderSt > 0)
			order = sql.substring(orderSt);
	}

	public String getSelect() {
		return select;
	}

	public String getFrom() {
		return from;
	}

	public String getOrder() {
		return order;
	}

	public static void main(String[] args) {
		CountHelper ch = new CountHelper(
				"select  debt.WARNING_FLAG,"
						+ " debt.CONTRACT_CODE,"
						+ " loan.FIN_AGREEMENT_ID,"
						+ " loan.ORDER_ID,"
						+ " debt.LOAN_BALANCE,"
						+ " debt.PLEDGE_BALANCE,"
						+ " debt.EVALUATE_VALUE,"
						+ " debt.DEBT_AMOUNT,"
						+ " ( select e.party_name from T_PEU_FIN_ORG e where e.party_id=agr.LENDER_ID)  LENDER_NAME,"
						+ "  agr.PLEDGE_RATE, "
						+ " ( select e.party_name from T_PEU_FIN_ORG e where e.party_id=agr.PLED_DIPOS_PARTY_ID) PLED_DIPOS_PARTY_NEME, "
						+ " agr.MARGIN_RATIO,"
						+ " ( select e.party_name from T_PEU_FIN_ORG e where e.party_id=agr.BORROWER_ID) BORROWER_NEME  " 
				     	+ " loan.VALID_FROM, "
						+ " loan.VALID_TO, "
						+ " loan.WAREHOUSE_NAME," 
						+ " (select e.party_name from T_PEU_FIN_ORG e where e.party_id=agr.GOODSSUPERV_PARTY_ID) GOODSSUPERV_PARTY_NEME"
						+ " from t_fin_contract_debt debt,t_fin_loan_contract loan,T_FIN_FINANCING_AGREEMENT agr"
						+ " where 1=1 and debt.CONTRACT_ID=loan.CONTRACT_ID and agr.fin_agreement_id=loan.fin_agreement_id"
						+ " #{ and loan.FIN_AGREEMENT_ID = :FinAgreementId}  #{ and lower(loan.CONTRACT_CODE) = :ContractCode}"
						+ " #{ and lower(loan.ORDER_ID) = :OrderId}"
						+ " #{and loan.valid_from >= :ValidFrom1 and loan.valid_from < :ValidFrom2}  "
						+ " #{and loan.valid_to >= :ValidTo1 and loan.valid_to < :ValidTo2} "
						+ " #{and loan.creator= :creator}"
						+ " #{order by :order :dir}");
		System.out.println(ch.getSelect());
		System.out.println(ch.getFrom());

	}
}
