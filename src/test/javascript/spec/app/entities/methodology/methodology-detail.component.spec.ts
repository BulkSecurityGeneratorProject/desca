/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { MethodologyDetailComponent } from '../../../../../../main/webapp/app/entities/methodology/methodology-detail.component';
import { MethodologyService } from '../../../../../../main/webapp/app/entities/methodology/methodology.service';
import { Methodology } from '../../../../../../main/webapp/app/entities/methodology/methodology.model';

describe('Component Tests', () => {

    describe('Methodology Management Detail Component', () => {
        let comp: MethodologyDetailComponent;
        let fixture: ComponentFixture<MethodologyDetailComponent>;
        let service: MethodologyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [MethodologyDetailComponent],
                providers: [
                    MethodologyService
                ]
            })
            .overrideTemplate(MethodologyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MethodologyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MethodologyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Methodology(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.methodology).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
