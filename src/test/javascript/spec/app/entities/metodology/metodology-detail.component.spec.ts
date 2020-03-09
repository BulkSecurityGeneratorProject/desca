/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { MetodologyDetailComponent } from '../../../../../../main/webapp/app/entities/metodology/metodology-detail.component';
import { MetodologyService } from '../../../../../../main/webapp/app/entities/metodology/metodology.service';
import { Metodology } from '../../../../../../main/webapp/app/entities/metodology/metodology.model';

describe('Component Tests', () => {

    describe('Metodology Management Detail Component', () => {
        let comp: MetodologyDetailComponent;
        let fixture: ComponentFixture<MetodologyDetailComponent>;
        let service: MetodologyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [MetodologyDetailComponent],
                providers: [
                    MetodologyService
                ]
            })
            .overrideTemplate(MetodologyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MetodologyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MetodologyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Metodology(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.metodology).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
