package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.dtos.pages.get.CanvasPageGetDTO;
import br.demo.backend.model.dtos.pages.get.PageGetDTO;
import br.demo.backend.model.pages.CanvasPage;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.utils.TransformSimple;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;

public class PageToGetDTO implements ModelToGetDTO<Page, PageGetDTO> {
    @Override
    public PageGetDTO tranform(Page page) {
        if(page == null) return null;
        PageGetDTO pageGet;
        if(page instanceof OrderedPage){
            pageGet = tranform((OrderedPage) page);
        }else if(page instanceof CanvasPage){
            pageGet = new CanvasPageGetDTO();
            BeanUtils.copyProperties(page, pageGet);
        }else{
            pageGet = new PageGetDTO();
            BeanUtils.copyProperties(page, pageGet);
        }
        try {
            pageGet.setProperties(page.getProperties().stream().map(TransformSimple::tranform).toList());
        } catch (NullPointerException ignore) {
            pageGet.setProperties(new ArrayList<>());
        }
        try {
            pageGet.setTasks(page.getTasks().stream().filter(t -> !t.getTask().getDeleted())
                    .map(TransformSimple::tranform).toList());
        }catch (NullPointerException ignore) {
            pageGet.setTasks(new ArrayList<>());
        }
        return pageGet;
    }
}
