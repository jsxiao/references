package com.aggrepoint.winlet.site;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;

import com.aggrepoint.winlet.AccessRuleEngine;
import com.aggrepoint.winlet.Context;
import com.aggrepoint.winlet.ContextUtils;
import com.aggrepoint.winlet.site.domain.Branch;
import com.aggrepoint.winlet.site.domain.Page;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
@Controller
public class SiteController {
	static final Log logger = LogFactory.getLog(SiteController.class);

	static final int CHECK_UPDATE_INTERVAL = 2000;

	static ServletContext context;

	static FileSystemCfgLoader loader;
	/** 分支配置 */
	static ArrayList<Branch> branches;

	private static void updateBranches() {
		if (context == null)
			context = Context.get().getBean(ServletContext.class);

		if (loader == null)
			loader = new FileSystemCfgLoader(
					context.getRealPath("/WEB-INF/site/branch"),
					CHECK_UPDATE_INTERVAL);

		branches = loader.load(branches);
	}

	public static Page getPage(AccessRuleEngine engine, String path) {
		updateBranches();
		Branch branch = null;
		for (Branch b : branches) {
			try {
				if (b.getRule() == null || engine.eval(b.getRule())) {
					branch = b;
					break;
				}
			} catch (Exception e) {
				logger.error(
						"Error evaluating branch access rule \"" + b.getRule()
								+ "\".", e);
			}
		}

		if (branch == null)
			return null;
		return branch.findPage(path, engine);
	}

	@RequestMapping("/site/**")
	public String site(HttpServletRequest req, AccessRuleEngine engine,HttpServletResponse resp) {
		try {
			Page page = getPage(engine, req.getServletPath().substring(5));
			if (page == null)
				return "/WEB-INF/site/error/pagenotfound.jsp";
			if (page.getLink() != null)
				return "redirect:" + page.getLink();
			String port = req.getHeader("Sour_Port");
			String params = req.getQueryString()==null?"":"?"+req.getQueryString();
			if (page.isUseHttps()){
				if(port!=null&&port.equals("80"))
					return ("redirect:" + ContextUtils.getReqInfo().getPageUrl().replaceFirst("http://", "https://")) + params;
			} else {
				if(port!=null&&port.equals("443"))
					return ("redirect:" + ContextUtils.getReqInfo().getPageUrl().replaceFirst("https://", "http://")) + params;
			}
			
			SiteContext sc = new SiteContext(req, page);
			req.setAttribute(SiteContext.SITE_CONTEXT_KEY, sc);

			return "/WEB-INF/site/template/" + page.getTemplate() + ".jsp";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
