/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { MetodologyComponent } from '../../../../../../main/webapp/app/entities/metodology/metodology.component';
import { MetodologyService } from '../../../../../../main/webapp/app/entities/metodology/metodology.service';
import { Metodology } from '../../../../../../main/webapp/app/entities/metodology/metodology.model';

describe('Component Tests', () => {

    describe('Metodology Management Component', () => {
        let comp: MetodologyComponent;
        let fixture: ComponentFixture<MetodologyComponent>;
        let service: MetodologyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [MetodologyComponent],
                providers: [
                    MetodologyService
                ]
            })
            .overrideTemplate(MetodologyComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MetodologyComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MetodologyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Metodology(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.metodologies[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
