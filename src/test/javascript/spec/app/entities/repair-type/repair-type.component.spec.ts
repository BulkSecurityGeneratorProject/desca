/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { RepairTypeComponent } from '../../../../../../main/webapp/app/entities/repair-type/repair-type.component';
import { RepairTypeService } from '../../../../../../main/webapp/app/entities/repair-type/repair-type.service';
import { RepairType } from '../../../../../../main/webapp/app/entities/repair-type/repair-type.model';

describe('Component Tests', () => {

    describe('RepairType Management Component', () => {
        let comp: RepairTypeComponent;
        let fixture: ComponentFixture<RepairTypeComponent>;
        let service: RepairTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [RepairTypeComponent],
                providers: [
                    RepairTypeService
                ]
            })
            .overrideTemplate(RepairTypeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RepairTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RepairTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new RepairType(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.repairTypes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
