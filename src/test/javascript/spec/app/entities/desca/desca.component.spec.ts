/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { DescaComponent } from '../../../../../../main/webapp/app/entities/desca/desca.component';
import { DescaService } from '../../../../../../main/webapp/app/entities/desca/desca.service';
import { Desca } from '../../../../../../main/webapp/app/entities/desca/desca.model';

describe('Component Tests', () => {

    describe('Desca Management Component', () => {
        let comp: DescaComponent;
        let fixture: ComponentFixture<DescaComponent>;
        let service: DescaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [DescaComponent],
                providers: [
                    DescaService
                ]
            })
            .overrideTemplate(DescaComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DescaComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DescaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Desca(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.descas[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
