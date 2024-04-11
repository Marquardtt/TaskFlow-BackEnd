package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.dtos.pages.get.OrderedPageGetDTO;
import br.demo.backend.model.dtos.relations.TaskPageGetDTO;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class OrderedPageToGetDTO implements ModelToGetDTO<OrderedPage, OrderedPageGetDTO> {
    private final PropertyToGetDTO propertyToGetDTO;

    public OrderedPageToGetDTO(PropertyToGetDTO propertyToGetDTO) {
        this.propertyToGetDTO = propertyToGetDTO;
    }

    @Override
    public OrderedPageGetDTO tranform(OrderedPage orderedPage) {
        if(orderedPage == null) return null;
        OrderedPageGetDTO pageGet = new OrderedPageGetDTO();
        BeanUtils.copyProperties(orderedPage, pageGet);
        pageGet.setPropertyOrdering(propertyToGetDTO.tranform(orderedPage.getPropertyOrdering()));
        return pageGet;
    }
}
