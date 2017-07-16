package net.ncue.spring;

public final class Paging {
	/**
	 * Don't let anyone instantiate this class
	 */
	private Paging () {
	}

	/**
	 * 
	 * @param totCnt
	 * @param viewNum
	 * @return
	 */
	public static int totPage (int totCnt, String viewNum) {
		int totPage = 0;
		int num = Integer.parseInt (viewNum);

		if (totCnt >= 1) {
			int nTemp = totCnt % num;
			if (nTemp == 0) {
				totPage = totCnt / num;
			} else {
				totPage = totCnt / num + 1;
			}
		} else {
			totPage = 0;
		}
		return totPage;
	}

	/**
	 * 
	 * @param totCnt
	 * @param curPage
	 * @param viewNum
	 * @return
	 */
	public static int startIdx (int totCnt, int curPage, String viewNum) {
		int i = 0;
		int totPage = totPage (totCnt, viewNum);

		if (totPage <= curPage) {
			curPage = totPage;
		}
		if (totPage == 0) {
			curPage = 0;
		}

		i = (curPage > 0 ? Integer.parseInt (viewNum) * (curPage - 1) : 0);

		return i;
	}

	/**
	 * 
	 * @param totCnt
	 * @param curPage
	 * @param viewNum
	 * @return
	 */
	public static int endIdx (int totCnt, int curPage, String viewNum) {
		int i = Integer.parseInt (viewNum);

		return i;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String makeLink (String value) {
		String link = null;
		int i = value.indexOf ("?");

		if (i < 0) {
			link = value + "?curPage=";
		} else {
			StringBuffer sb = new StringBuffer ();
			sb.append (value.substring (0, i + 1));
			String[] val = value.substring (i + 1).split ("&");

			for (int j = 0; j < val.length; j++) {

				if (val[j].startsWith ("curPage")) {
					continue;
				} else {
					sb.append (val[j]);
					sb.append ("&");
				}
			}

			link = sb.toString () + "curPage=";
		}

		return link;
	}
}