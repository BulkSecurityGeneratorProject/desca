/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { JudicialProcessTypeComponent } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type.component';
import { JudicialProcessTypeService } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type.service';
import { JudicialProcessType } from '../../../../../../main/webapp/app/entities/judicial-process-type/judicial-process-type.model';

describe('Component Tests', () => {

    describe('JudicialProcessType Management Component', () => {
        let comp: JudicialProcessTypeComponent;
        let fixture: ComponentFixture<JudicialProcessTypeComponent>;
        let service: JudicialProcessTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [JudicialProcessTypeComponent],
                providers: [
                    JudicialProcessTypeService
                ]
            })
            .overrideTemplate(JudicialProcessTypeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JudicialProcessTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JudicialProcessTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new JudicialProcessType(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.judicialProcessTypes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
