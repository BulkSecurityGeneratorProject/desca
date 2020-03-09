/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { DescaDetailComponent } from '../../../../../../main/webapp/app/entities/desca/desca-detail.component';
import { DescaService } from '../../../../../../main/webapp/app/entities/desca/desca.service';
import { Desca } from '../../../../../../main/webapp/app/entities/desca/desca.model';

describe('Component Tests', () => {

    describe('Desca Management Detail Component', () => {
        let comp: DescaDetailComponent;
        let fixture: ComponentFixture<DescaDetailComponent>;
        let service: DescaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [DescaDetailComponent],
                providers: [
                    DescaService
                ]
            })
            .overrideTemplate(DescaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DescaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DescaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Desca(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.desca).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
