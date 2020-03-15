/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { RepairComponent } from '../../../../../../main/webapp/app/entities/repair/repair.component';
import { RepairService } from '../../../../../../main/webapp/app/entities/repair/repair.service';
import { Repair } from '../../../../../../main/webapp/app/entities/repair/repair.model';

describe('Component Tests', () => {

    describe('Repair Management Component', () => {
        let comp: RepairComponent;
        let fixture: ComponentFixture<RepairComponent>;
        let service: RepairService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [RepairComponent],
                providers: [
                    RepairService
                ]
            })
            .overrideTemplate(RepairComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RepairComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RepairService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Repair(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.repairs[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
