/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { JudicialProcessTypeDetailComponent } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type-detail.component';
import { JudicialProcessTypeService } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type.service';
import { JudicialProcessType } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type.model';

describe('Component Tests', () => {

    describe('JudicialProcessType Management Detail Component', () => {
        let comp: JudicialProcessTypeDetailComponent;
        let fixture: ComponentFixture<JudicialProcessTypeDetailComponent>;
        let service: JudicialProcessTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [JudicialProcessTypeDetailComponent],
                providers: [
                    JudicialProcessTypeService
                ]
            })
            .overrideTemplate(JudicialProcessTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JudicialProcessTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JudicialProcessTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new JudicialProcessType(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.judicialProcessType).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
