package br.demo.backend.service.properties;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.pages.OrderedPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Date;
import br.demo.backend.model.properties.Limited;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.properties.Select;
import br.demo.backend.repository.properties.DateRepository;
import br.demo.backend.repository.properties.LimitedRepository;
import br.demo.backend.repository.properties.SelectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@AllArgsConstructor
@Service
public class DefaultPropsService {

    private DateRepository dateRepository;
    private SelectRepository selectRepository;
    private LimitedRepository limitedRepository;

    public Select select(Project project, Page page){
        ArrayList<Option> options = new ArrayList<>();
        options.add(new Option(null, "To-do", "#FF7A00", 0));
        options.add(new Option(null, "Doing", "#F7624B", 1));
        options.add(new Option(null, "Done", "#F04A94", 2));
        ArrayList<Page> pages = new ArrayList<>();
        pages.add(page);
        Select select = new Select(null, "Stats", true, false,
                options, TypeOfProperty.SELECT, pages, project);
        page.setProperties(new ArrayList<>());
        select = selectRepository.save(select);
        page.getProperties().add(select);
        return select;
    }

    public Date date(Project project, Page page){
        ArrayList<Page> pages = new ArrayList<>();
        pages.add(page);
        Date date = new Date(null, "Date", true, false, pages, project);
        page.setProperties(new ArrayList<>());
        Date dateSaved = dateRepository.save(date);
        page.getProperties().add(dateSaved);
        return dateSaved;
    }

    public Limited limited(Project project, OrderedPage page) {
        ArrayList<Page> pages = new ArrayList<>();
        pages.add(page);
        Limited limited = new Limited(null, "Time", true, false,
                TypeOfProperty.TIME, pages, project, 28800L );
        page.setProperties(new ArrayList<>());
        Limited limitedSaved = limitedRepository.save(limited);
        page.getProperties().add(limitedSaved);
        return limitedSaved;
    }
}
