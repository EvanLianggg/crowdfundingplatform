package com.atguigu.crowd.mvc.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;

// @Controller
// @ResponseBody
@RestController
public class MenuHandler {
	
	@Autowired
	private MenuService menuService;
	
	// @ResponseBody
	@RequestMapping("/menu/remove.json")
	public ResultEntity<String> removeMenu(@RequestParam("id") Integer id) {
		
		menuService.removeMenu(id);
		
		return ResultEntity.successWithoutData();
	}
	
	// @ResponseBody
	@RequestMapping("/menu/update.json")
	public ResultEntity<String> updateMenu(Menu menu) {
		
		menuService.updateMenu(menu);
		
		return ResultEntity.successWithoutData();
	}
	
	// @ResponseBody
	@RequestMapping("/menu/save.json")
	public ResultEntity<String> saveMenu(Menu menu) {
		
		// Thread.sleep(2000);
		
		menuService.saveMenu(menu);
		
		return ResultEntity.successWithoutData();
	}
	
	// @ResponseBody
	@RequestMapping("/menu/get/whole/tree.json")
	public ResultEntity<Menu> getWholeTreeNew() {
		
		// 1. Query all Menu objects

		List<Menu> menuList = menuService.getAll();
	
		// 2. Declare a variable to store the root node found
		Menu root = null;
		
		// 3. Create a Map object to store the correspondence between id and Menu objects to find the parent node
		Map<Integer, Menu> menuMap = new HashMap<>();
		
		// 4. Traverse menuList to fill menuMap
		for (Menu menu : menuList) {
			
			Integer id = menu.getId();
			
			menuMap.put(id, menu);
		}
		
		// 5. Traverse the menuList again to find the root node and assemble the parent and child nodes
		for (Menu menu : menuList) {
			
			// 6. Get the pid attribute value of the current menu object
			Integer pid = menu.getPid();
			
			// 7. If pid is null, judge as the root node
			if(pid == null) {
				root = menu;
				
				// 8. If the current node is the root node, then there must be no parent node, no need to continue execution
				continue ;
			}
			
			// 9. If pid is not null, indicating that the current node has a parent node, then you can find the corresponding Menu object in menuMap according to pid
			Menu father = menuMap.get(pid);
			
			// 10. Save the current node into the children collection of the parent node
			father.getChildren().add(menu);
		}
		
		// 11. After the above calculation, the root node contains the entire tree structure, and returning the root node is returning the entire tree
		return ResultEntity.successWithData(root);
	}
	
	public ResultEntity<Menu> getWholeTreeOld() {
		
		// 1. Query all Menu objects
		List<Menu> menuList = menuService.getAll();
		
		// 2. Declare a variable to store the root node found
		Menu root = null;
		
		// 3. Traverse the menuList
		for (Menu menu : menuList) {
			
			// 4. Get the pid attribute value of the current menu object
			Integer pid = menu.getPid();
			
			// 5. Check if pid is null
			if(pid == null) {
				
				// 6. Assign the menu object currently being traversed to root
				root = menu;
				
				// 7. Stop this cycle and continue to execute the next cycle
				continue ;
			}
			
			// 8. If pid is not null, it means that the current node has a parent node, and the parent node can be found to be assembled to establish a parent-child relationship
			for (Menu maybeFather : menuList) {
				
				// 9. Get the id attribute of maybeFather
				Integer id = maybeFather.getId();
				
				// 10. Compare the pid of the child node with the id of the suspected parent node
				if(Objects.equals(pid, id)) {
					
					// 11.Store the child node in the children collection of the parent node
					maybeFather.getChildren().add(menu);
					
					// 12. Find it to stop the running loop
					break ;
				}
				
			}
		}
		
		// 13. Return the assembled tree structure (that is, the root node object) to the browser
		return ResultEntity.successWithData(root);
	}

}
