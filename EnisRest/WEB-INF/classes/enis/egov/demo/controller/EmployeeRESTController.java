package enis.egov.demo.controller;

 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.SampleDefaultVO;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import enis.egov.demo.model.xml.EmployeeListVO;
import enis.egov.demo.model.xml.EmployeeVO;


@RestController
@RequestMapping(value = "/enisapi")
public class EmployeeRESTController 
{

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRESTController.class);

	/** EgovSampleService */
	@Resource(name = "sampleService")
	private EgovSampleService sampleService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	

    @RequestMapping(value = "/egovSampleList/{pageType}", headers = {"content-type=application/json"})
	public @ResponseBody Map<String, Object> selectSampleList(@PathVariable("pageType") String pageType, 
			@RequestParam(value="delayTime", required=false) String sDelayTime,
			@ModelAttribute("searchVO") SampleDefaultVO searchVO, 
			ModelMap model) throws Exception {
    	LOGGER.debug(MarkerFactory.getMarker("RestService")," EmployeeRESTController egovSampleList called...............");
    	
    	Map<String, Object> mapPageType = new HashMap<String,Object> ();
    	if("parking".equals(pageType)) {
    		mapPageType.put("pageName", "주정차위반과태료 대장");
    		
    	} else if("penalty".equals(pageType)) {
    		mapPageType.put("pageName", "과태료/과징금 대장");
    	} else if("levy".equals(pageType)) {
    		mapPageType.put("pageName", "부과대장");
    	} else {
    		mapPageType.put("pageName", "게시판");
    	}
    	int delayTime= 0;
    	if(sDelayTime != null && !sDelayTime.isEmpty())
    		delayTime = Integer.parseInt(sDelayTime);
    	
    	mapPageType.put("service delay", delayTime+"");
    	 
    	LOGGER.debug(MarkerFactory.getMarker("RestService")," EmployeeRESTController egovSampleList delayTime ["+delayTime+"]");
    	if (delayTime >0) {
    		LOGGER.debug(MarkerFactory.getMarker("RestService")," EmployeeRESTController egovSampleList sleep start.");
    		Thread.sleep(delayTime);
    		LOGGER.debug(MarkerFactory.getMarker("RestService")," EmployeeRESTController egovSampleList is awaken.");
    	}
    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");


		/** EgovPropertyService.sample */
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<?> sampleList = sampleService.selectSampleList(searchVO);
	
		mapPageType.put("result", sampleList);
		ThreadContext.put("logger.data", sampleList.toString());
		LOGGER.debug(MarkerFactory.getMarker("RestService")," EmployeeRESTController egovSampleList completed:...............");
        
        ThreadContext.clearAll();
        
        
        return mapPageType;
	}    
    
    @RequestMapping(value = "/payers", headers = {"content-type=application/xml"})
    public @ResponseBody EmployeeListVO getAllEmployees() 
    {
    	EmployeeListVO employees = new EmployeeListVO();
         
    	EmployeeVO empOne = new EmployeeVO(1,"홍","길동","howtodoinjava@gmail.com");
    	EmployeeVO empTwo = new EmployeeVO(2,"강","동원","asinghal@yahoo.com");
    	EmployeeVO empThree = new EmployeeVO(3,"유","재석","kmishra@gmail.com");
         
         
        employees.getEmployees().add(empOne);
        employees.getEmployees().add(empTwo);
        employees.getEmployees().add(empThree);
         
        return employees;
    }
     
    @RequestMapping(value = "/payers/{id}", headers = {"content-type=application/xml"})
    @ResponseBody
    public ResponseEntity<EmployeeVO> getEmployeeById (@PathVariable("id") int id) 
    {
        if (id <= 3) {
        	EmployeeVO employee = new EmployeeVO(1,"홍","길동","howtodoinjava@gmail.com");
            return new ResponseEntity<EmployeeVO>(employee, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    
    

    @RequestMapping(value = "/payers/{id}", headers = {"content-type=application/json"})
    @ResponseBody
    public  ResponseEntity<enis.egov.demo.model.json.EmployeeVO> getEmployeeByIdJson (@PathVariable("id") int id) 
    {
    	 if (id <= 3) {
        	enis.egov.demo.model.json.EmployeeVO employee = null;
        	if(id==1) employee  = new enis.egov.demo.model.json.EmployeeVO(1,"홍","길동","howtodoinjava@gmail.com");
        	else if(id==2) employee  = new enis.egov.demo.model.json.EmployeeVO(2,"강","동원","asinghal@yahoo.com");
        	else if(id==3) employee  = new enis.egov.demo.model.json.EmployeeVO(3,"유","재석","kmishra@gmail.com");
        	  return new ResponseEntity<enis.egov.demo.model.json.EmployeeVO>(employee, HttpStatus.OK);
    	 }
    	 return new ResponseEntity(HttpStatus.NOT_FOUND);
       
    }
}